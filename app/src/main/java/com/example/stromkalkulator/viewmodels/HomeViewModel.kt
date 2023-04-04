package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.ViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel: ViewModel() {
    val httpClient: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }

    private var homeState = MutableStateFlow<HomeUiState>(HomeUiState())
    val homeStateFlow: StateFlow<HomeUiState> = homeState.asStateFlow()
}
class HomeUiState {}