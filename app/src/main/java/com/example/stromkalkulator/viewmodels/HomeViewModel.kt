package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedViewModel: SharedViewModel
): ViewModel() {
    private val httpClient: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }

//    private var homeState = MutableStateFlow(HomeUiState(listOf(), listOf(), "0.0"))
//    val homeStateFlow: StateFlow<HomeUiState> = homeState.asStateFlow()

    fun getCurrentPrice(): StateFlow<SharedUiState> {
        sharedViewModel.getCurrentPrice()
        return sharedViewModel.sharedStateFlow
    }

    fun getRegion() = sharedViewModel.sharedStateFlow.value.region

    fun getTemperatures() = sharedViewModel.sharedStateFlow.value.temperatures

    fun getPrices() = sharedViewModel.sharedStateFlow.value.prices
}

//data class HomeUiState()