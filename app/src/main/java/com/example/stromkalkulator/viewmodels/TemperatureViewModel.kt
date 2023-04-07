package com.example.stromkalkulator.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stromkalkulator.data.models.Temperature
import com.example.stromkalkulator.data.repositories.TemperatureRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import java.io.IOException


@Deprecated("Only one viewModel is needed per screen",ReplaceWith("HomeViewModel"))
class TemperatureViewModel : ViewModel() {
    private val httpClient: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }
    private val repository = TemperatureRepository(httpClient)
    var temperatureList = emptyList<Temperature>()

    init {
        viewModelScope.launch {
            getTemperatureList(59.911491, 10.757933)
        }
    }

    suspend fun getTemperatureList(lat: Double, lon: Double): Boolean {
        return try {
            temperatureList = repository.getTemperatureData(lat, lon)
            true
        } catch (e: IOException) {
            Log.e(ContentValues.TAG, "API call failed: ${e.message}")
            false
        }
    }

}