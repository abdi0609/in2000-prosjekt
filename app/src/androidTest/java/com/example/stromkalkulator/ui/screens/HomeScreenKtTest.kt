package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.stromkalkulator.viewmodels.HomeViewModel
import com.example.stromkalkulator.viewmodels.SharedUiState
import com.example.stromkalkulator.viewmodels.SharedViewModel
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar


@RunWith(AndroidJUnit4::class)
internal class HomeScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun homeScreen() = runTest() {
        // Mock required dependencies
        val mockPaddingValues = PaddingValues()
        val mockSharedViewModel = mockk<SharedViewModel>().apply {
            every { sharedStateFlow } returns MutableStateFlow(
                SharedUiState(
                    temperatures = listOf(),
                    prices = listOf(),
                    currentPrice = "0.5"
                )
            )
        }
        val mockHomeViewModel = mockk<HomeViewModel>().apply {
            every { getRegion() } returns mockSharedViewModel.sharedStateFlow.value.region
            every { getTemperatures() } returns mockSharedViewModel.sharedStateFlow.value.temperatures
            every { getPrices() } returns mockSharedViewModel.sharedStateFlow.value.prices
            every { getCurrentPrice() } returns mockSharedViewModel.sharedStateFlow
        }

        mockkStatic(Calendar::class)
        val mockedCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 3)
            set(Calendar.DAY_OF_MONTH, 7)
            set(Calendar.DAY_OF_YEAR, 97)
            set(Calendar.HOUR_OF_DAY, 15)
        }
        every { Calendar.getInstance() } returns mockedCalendar

        // Set the content
        composeTestRule.setContent {
            HomeScreen(mockPaddingValues, mockHomeViewModel)
        }

        // Verify that the content is displayed
        composeTestRule.onNodeWithText("0.5 kr/kwh").assertExists()
    }

    /**
     * Creates a mock HttpClient with the specified response.
     *
     * This function sets up an HttpClient with a MockEngine to return the provided result when
     * regardless of the URL requested.
     *
     * @param result The mock response (as a JSON string) to return.
     * @return A mock HttpClient configured with the specified response.
     */
    private fun createMockClient(result: String): HttpClient {
        return HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json()
            }
            engine {
                addHandler {
                    respond(
                        result,
                        headers = headersOf(
                            "Content-Type" to listOf("application/json")
                        )
                    )
                }
            }
        }
    }
}