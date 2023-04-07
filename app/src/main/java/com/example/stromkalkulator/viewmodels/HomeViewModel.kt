package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stromkalkulator.data.models.Temperature
import com.example.stromkalkulator.data.models.electricity.HourPrice
import com.example.stromkalkulator.data.repositories.ElectricityPrice
import com.example.stromkalkulator.data.repositories.TemperatureRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    val httpClient: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }
    private val temperatureRepository = TemperatureRepository(httpClient)
    private val electricityPrice = ElectricityPrice(httpClient)

    private var homeState = MutableStateFlow(HomeUiState(listOf(), listOf()))
    val homeStateFlow: StateFlow<HomeUiState> = homeState.asStateFlow()

    init {
        viewModelScope.launch {
            homeState.update {
                it.copy(
                    temperatures = temperatureRepository.getTemperatureData(50.0,10.0),
                    // TODO: This is messy, find a better raw -> presentable data method
                    // TODO: Make the region dynamic
                    // Adds all the week's day's hourprices to a list
                    prices = electricityPrice.getWeek(ElectricityPrice.Region.NO1)
                        .days.fold(mutableListOf()) { acc, i ->
                        acc.addAll(i.hours)
                        acc
                    }
                )
            }
        }
    }
}

data class HomeUiState(
    val temperatures: List<Temperature>,
    val prices: List<HourPrice>
)