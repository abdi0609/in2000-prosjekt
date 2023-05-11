package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.ViewModel
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
import java.util.*

class CalculatorViewModel : GenericViewModel() {

    private var calculatorState = MutableStateFlow<CalculatorUiState>(CalculatorUiState())
    val calculatorStateFlow: StateFlow<CalculatorUiState> = calculatorState.asStateFlow()

    init {
        updateTodaysPrices()
        updateCurrentHour()
    }

    private fun updateTodaysPrices() {
        viewModelScope.launch {
            calculatorState.update { state ->
                state.copy(
                    pricesToday = ElectricityPriceDomain.getToday()
                )
            }
            // TODO: ???
            assert(calculatorState.value.pricesToday.size in listOf(0,24))

        }
    }

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

    override fun setRegion(region: Region) {
        RegionSingleton.region = region
        calculatorState.update {
            it.copy(region = region)
        }
    }

    override fun updateTempsAndPrices() {
        updateTodaysPrices()
    }
}

data class CalculatorUiState(
    val pricesToday: List<Double> = listOf(),
    val currentHour: Int = 0,
    val region: Region = RegionSingleton.region,

)