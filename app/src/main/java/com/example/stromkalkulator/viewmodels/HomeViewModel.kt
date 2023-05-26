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

/**
 * This ViewModel class acts as a bridge between the HomeScreen and the domain layer.
 *
 * @param regionDomain The RegionDomain to use.
 */
class HomeViewModel(
    private val regionDomain: RegionDomain
) : GenericViewModel() {

    private var homeState = MutableStateFlow(HomeUiState())
    val homeStateFlow: StateFlow<HomeUiState> = homeState.asStateFlow()

    init {
        updateRegion()
        updateTempsAndPrices()

    }

    /**
     * Sets the region to the given region.
     *
     * @param region The region to set.
     */
    override fun setRegion(region: Region) {
        viewModelScope.launch {
            regionDomain.setRegion(region)
            homeState.update {
                it.copy(region = region)
            }
        }
    }

    /**
     * Updates the homeState with the cached prices and temperatures.
     */
    override fun updateTempsAndPrices() {
        viewModelScope.launch {
            // TODO: Implement options for how to view the graph (week, month, year).
            // Currently the week is hardcoded, but the domain layer is already prepared for this.
            val (price,temp) = GraphHelperDomain.getPresentableWeekPair(
                homeState.value.region
            )
            homeState.update {
                it.copy(
                    currentPrice = ElectricityPriceDomain.getCurrentHour(homeState.value.region),
                    presentablePrices = price,
                    presentableTemperatures = temp
                )
            }
        }
    }

    /**
     * Updates the homeState with the current region.
     */
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

    /**
     * Factory for creating a HomeViewModel.
     */
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