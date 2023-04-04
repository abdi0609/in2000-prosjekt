package com.example.stromkalkulator.data.repositories

import com.example.stromkalkulator.BuildConfig
import com.example.stromkalkulator.data.models.Temperature
import com.example.stromkalkulator.data.models.WeatherData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TemperatureRepository(private val httpClient: HttpClient) {
    private val baseUrl = "https://api.met.no/weatherapi/locationforecast/2.0/compact"
    private val apiKey = BuildConfig.apiKey
    suspend fun getTemperatureData(lat: Double, lon: Double): List<Temperature>
    = withContext(Dispatchers.IO) {
        val url = "$baseUrl?lat=$lat&lon=$lon"
        return@withContext try {
            val request = httpClient.get(url) {
                headers {
                    append(HttpHeaders.Authorization,apiKey)
                }
            }
            val data = request.body<WeatherData>()
            data.properties.timeseries.fold(
                mutableListOf<Temperature>()
            ) { acc, i ->
                acc.add(Temperature(i.time, i.data.instant.details.air_temperature))
                acc
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}