package com.example.stromkalkulator.data.repositories

import com.example.stromkalkulator.BuildConfig
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.models.WeatherData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TemperatureRepository {
    private val httpClient: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }

    private const val baseUrl = "https://api.met.no/weatherapi/locationforecast/2.0/compact"
    private const val apiKey = BuildConfig.apiKey

    private fun latLongPairFrom(
        region: Region
    ): Pair<String, String> =
        when (region) {
            Region.NO1 -> "59.91" to "10.75"
            Region.NO2 -> "58.15" to "8.00"
            Region.NO3 -> "63.43" to "10.40"
            Region.NO4 -> "69.65" to "18.96"
            Region.NO5 -> "60.39" to "5.32"
        }

    suspend fun getWeatherData(
        region: Region,
        localHttpClient: HttpClient = httpClient
    ): WeatherData? = withContext(Dispatchers.IO) {
        val (lat, lon) = latLongPairFrom(region)
        val url = "$baseUrl?lat=$lat&lon=$lon"
        return@withContext try {
            localHttpClient.get(url) {
                headers {
                    append(HttpHeaders.Authorization,apiKey)
                }
            }.body<WeatherData>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}