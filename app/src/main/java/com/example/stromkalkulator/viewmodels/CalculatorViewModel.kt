package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.ViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel : ViewModel() {
    val httpClient: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }

    private var calculatorState = MutableStateFlow<CalculatorUiState>(CalculatorUiState())
    val calculatorStateFlow: StateFlow<CalculatorUiState> = calculatorState.asStateFlow()
}

class CalculatorUiState {}