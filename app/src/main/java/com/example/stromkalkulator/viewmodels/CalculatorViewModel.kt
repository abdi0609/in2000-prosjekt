package com.example.stromkalkulator.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stromkalkulator.StromKalkulatorApplication
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.domain.ElectricityPriceDomain
import com.example.stromkalkulator.domain.RegionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class CalculatorViewModel(
    private val regionRepository: RegionRepository
) : GenericViewModel() {

    private var calculatorState = MutableStateFlow<CalculatorUiState>(CalculatorUiState())
    val calculatorStateFlow: StateFlow<CalculatorUiState> = calculatorState.asStateFlow()

    init {
        updateRegion()
        updateCurrentHour()
        updateTodaysPrices()
    }

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
        viewModelScope.launch {
            regionRepository.setRegion(region)
            calculatorState.update {
                it.copy(region = region)
            }
        }
    }

    private fun updateRegion() {
        viewModelScope.launch {
            regionRepository.getRegion().collect { region ->
                calculatorState.update { state ->
                    Log.v("HomeViewModel", "Region: $region")
                    state.copy(region = region)
                }
            }
        }
    }


    override fun updateTempsAndPrices() {
        updateRegion()
        updateCurrentHour()
        updateTodaysPrices()
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StromKalkulatorApplication)
                CalculatorViewModel(
                    application.regionRepository
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