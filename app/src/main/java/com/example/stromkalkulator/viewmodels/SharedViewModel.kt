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
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//@Deprecated("Bad")
//class SharedViewModel @Inject constructor(): ViewModel() {
//    private var sharedState = MutableStateFlow(SharedUiState(
//    region = ElectricityPrice.Region.NO1,
//        temperatures = listOf(),
//        prices = listOf(),
//        currentPrice = "0.0"
//    ))
//    val sharedStateFlow: StateFlow<SharedUiState> = sharedState.asStateFlow()
//    private val httpClient: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }
//    private val temperatureRepository = TemperatureRepository(httpClient)
//    private val electricityPrice = ElectricityPrice(httpClient)
//
//    init {
//        updateTempsAndPrices()
//    }
//
//    fun updateTempsAndPrices() {
//        viewModelScope.launch {
//            sharedState.update {
//                it.copy(
//                    temperatures = temperatureRepository.getTemperatureData(50.0, 10.0),
//                    // TODO: This is messy, find a better raw -> presentable data method
//                    // Adds all the week's day's hourprices to a list
//                    prices = electricityPrice.getWeek(sharedStateFlow.value.region)
//                        .days.fold(mutableListOf()) { acc, i ->
//                            acc.addAll(i.hours)
//                            acc
//                        }
//                )
//            }
//        }
//    }
//
//    fun setRegion(region: ElectricityPrice.Region) {
//        sharedState.value = SharedUiState(region = region, sharedState.value.temperatures,
//            sharedState.value.prices, sharedState.value.currentPrice)
//    }
//
//    fun getCurrentPrice() {
//        viewModelScope.launch {
//            sharedState.update {
//                it.copy(currentPrice = electricityPrice.getCurrent(
//                    sharedStateFlow.value.region))
//            }
//        }
//    }
//}
//
//data class SharedUiState (
//    val region: ElectricityPrice.Region = ElectricityPrice.Region.NO1,
//    val temperatures: List<Temperature>,
//    val prices: List<HourPrice>,
//    val currentPrice: String,
//)
