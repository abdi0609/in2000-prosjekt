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
import com.example.stromkalkulator.domain.RegionRepository
import com.patrykandpatrick.vico.core.entry.ChartEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val regionRepository: RegionRepository
) : GenericViewModel() {

    private var homeState = MutableStateFlow(HomeUiState())
    val homeStateFlow: StateFlow<HomeUiState> = homeState.asStateFlow()

    init {
        updateTempsAndPrices()
        updateRegion()

    }

    override fun setRegion(region: Region) {
        viewModelScope.launch {
            regionRepository.setRegion(region)
            homeState.update {
                it.copy(region = region)
            }
        }
    }

    override fun updateTempsAndPrices() {
        // TODO: fix plz
        viewModelScope.launch {
            val (price,temp) = GraphHelperDomain.getPresentableMonthPair(
                homeState.value.region
            )
            println("\n\n$price\n$temp\n\n")
            homeState.update {
                it.copy(
                    currentPrice = ElectricityPriceDomain.getToday(homeState.value.region)[0],
                    presentablePrices = price,
                    presentableTemperatures = temp
                )
            }
        }
    }

    private fun updateRegion() {
        viewModelScope.launch {
            regionRepository.getRegion().collect { region ->
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
                    application.regionRepository
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