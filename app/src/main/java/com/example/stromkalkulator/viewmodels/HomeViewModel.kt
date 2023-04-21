package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.domain.ElectricityPriceDomain
import com.example.stromkalkulator.domain.GraphHelperDomain
import com.example.stromkalkulator.domain.RegionSingleton
import com.patrykandpatrick.vico.core.entry.ChartEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
): GenericViewModel() {

    private var homeState = MutableStateFlow(HomeUiState())
    val homeStateFlow: StateFlow<HomeUiState> = homeState.asStateFlow()

    init {
        updateTempsAndPrices()
    }

    override fun setRegion(region: Region) {
        RegionSingleton.region = region
        homeState.update {
            it.copy(region = region)
        }
    }

    override fun updateTempsAndPrices() {
        // TODO: fix plz
        viewModelScope.launch {
            val (price,temp) = GraphHelperDomain.getPresentableMonthPair()
            println("\n\n$price\n$temp\n\n")
            homeState.update {
                it.copy(
                    currentPrice = ElectricityPriceDomain.getToday()[0],
                    presentablePrices = price,
                    presentableTemperatures = temp
                )
            }
        }
    }

//    fun getCurrentPrice(): StateFlow<SharedUiState> {
////        sharedViewModel.getCurrentPrice()
////        return sharedViewModel.sharedStateFlow
//
//    }

//    fun getRegion() = homeStateFlow.value.region
//
//    fun getTemperatures() = sharedViewModel.sharedStateFlow.value.temperatures
//
//    fun getPrices() = sharedViewModel.sharedStateFlow.value.prices
}

data class HomeUiState(
    val region: Region = RegionSingleton.region,
    val currentPrice: Double = 0.0,
    val presentableTemperatures: List<ChartEntry> = listOf(),
    val presentablePrices: List<ChartEntry> = listOf()
)