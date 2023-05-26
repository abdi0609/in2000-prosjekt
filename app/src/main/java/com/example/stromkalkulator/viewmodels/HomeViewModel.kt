package com.example.stromkalkulator.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stromkalkulator.StromKalkulatorApplication
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.domain.ElectricityPriceDomain
import com.example.stromkalkulator.domain.GraphHelperDomain
import com.example.stromkalkulator.domain.RegionDomain
import com.patrykandpatrick.vico.core.entry.ChartEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val regionDomain: RegionDomain
) : GenericViewModel() {

    private var homeState = MutableStateFlow(HomeUiState())
    val homeStateFlow: StateFlow<HomeUiState> = homeState.asStateFlow()

    init {
        updateRegion()
        updateTempsAndPrices()

    }

    override fun setRegion(region: Region) {
        viewModelScope.launch {
            regionDomain.setRegion(region)
            homeState.update {
                it.copy(region = region)
            }
        }
    }

    override fun updateTempsAndPrices() {
        // TODO: fix plz
        viewModelScope.launch {
            val (price,temp) = GraphHelperDomain.getPresentableWeekPair(
                homeState.value.region
            )
            println("\n\n$price\n$temp\n\n")
            homeState.update {
                it.copy(
                    currentPrice = ElectricityPriceDomain.getCurrentHour(homeState.value.region),
                    presentablePrices = price,
                    presentableTemperatures = temp
                )
            }
        }
    }

    private fun updateRegion() {
        viewModelScope.launch {
            regionDomain.getRegion().collect { region ->
                homeState.update { state ->
                    Log.v("HomeViewModel", "Region: $region")
                    state.copy(region = region)
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as StromKalkulatorApplication)
                HomeViewModel(
                    application.regionDomain
                )

            }
        }
    }
}

data class HomeUiState(
    val region: Region = Region.NO1,
    val currentPrice: Double = 0.0,
    val presentableTemperatures: List<ChartEntry> = listOf(),
    val presentablePrices: List<ChartEntry> = listOf()
)