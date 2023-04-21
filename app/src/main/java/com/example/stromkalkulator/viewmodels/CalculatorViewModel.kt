package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel : ViewModel() {

    private var calculatorState = MutableStateFlow<CalculatorUiState>(CalculatorUiState())
    val calculatorStateFlow: StateFlow<CalculatorUiState> = calculatorState.asStateFlow()
}

class CalculatorUiState {}