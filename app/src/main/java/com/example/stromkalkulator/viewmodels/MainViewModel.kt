package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.ViewModel
import com.example.stromkalkulator.data.repositories.ElectricityPrice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
        private var mainState = MutableStateFlow(MainUiState())
        val mainStateFlow: StateFlow<MainUiState> = mainState.asStateFlow()

    fun setRegion(region: ElectricityPrice.Region) {
        mainState.value = MainUiState(region)
    }
}

data class MainUiState (
    val region: ElectricityPrice.Region = ElectricityPrice.Region.NO1
)
