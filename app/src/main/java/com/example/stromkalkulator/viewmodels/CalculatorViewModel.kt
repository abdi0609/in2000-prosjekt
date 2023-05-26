package com.example.stromkalkulator.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stromkalkulator.StromKalkulatorApplication
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.domain.ElectricityPriceDomain
import com.example.stromkalkulator.domain.RegionDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

/**
 * This ViewModel class acts as a bridge between the CalculatorScreen and the domain layer.
 *
 * @param regionDomain The RegionDomain to use.
 */
class CalculatorViewModel(
    private val regionDomain: RegionDomain
) : GenericViewModel() {

    private var calculatorState = MutableStateFlow(CalculatorUiState())
    val calculatorStateFlow: StateFlow<CalculatorUiState> = calculatorState.asStateFlow()

    init {
        updateRegion()
        updateCurrentHour()
        updateTodaysPrices()
    }

    /**
     * Updates the calculatorState with the current prices for the day.
     */
    private fun updateTodaysPrices() {
        viewModelScope.launch {
            calculatorState.update { state ->
                state.copy(
                    pricesToday = ElectricityPriceDomain.getToday(calculatorState.value.region)
                )
            }
            // TODO: ???
            assert(calculatorState.value.pricesToday.size in listOf(0,24))

        }
    }

    /**
     * Updates the calculatorState with the current hour.
     */
    private fun updateCurrentHour() {
        val calendar = Calendar.getInstance()
        viewModelScope.launch {
            calculatorState.update { state ->
                state.copy(
                    currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                )
            }
        }
    }

    /**
     * Sets the region in the domain layer and updates the calculatorState.
     *
     * @param region The region to set.
     */
    override fun setRegion(region: Region) {
        viewModelScope.launch {
            regionDomain.setRegion(region)
            calculatorState.update {
                it.copy(region = region)
            }
        }
    }

    /**
     * Updates the calculatorState with the current region.
     */
    private fun updateRegion() {
        viewModelScope.launch {
            regionDomain.getRegion().collect { region ->
                calculatorState.update { state ->
                    Log.v("HomeViewModel", "Region: $region")
                    state.copy(region = region)
                }
            }
        }
    }


    /**
     * Updates the calculatorState with the current region, temperatures and prices.
     */
    override fun updateTempsAndPrices() {
        updateRegion()
        updateCurrentHour()
        updateTodaysPrices()
    }

    /**
     * Factory for the CalculatorViewModel.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StromKalkulatorApplication)
                CalculatorViewModel(
                    application.regionDomain
                )

            }
        }
    }
}

data class CalculatorUiState(
    val pricesToday: List<Double> = listOf(),
    val currentHour: Int = 0,
    val region: Region = Region.NO1,

)