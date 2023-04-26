package com.example.stromkalkulator.domain

import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.models.Temperature
import com.example.stromkalkulator.data.models.WeatherData
import com.example.stromkalkulator.data.repositories.TemperatureRepository
import io.ktor.util.date.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TemperatureDomain {

    private val lastUpdatedMap: MutableMap<Region, Long> = mutableMapOf(
        Region.NO1 to 0,
        Region.NO2 to 0,
        Region.NO3 to 0,
        Region.NO4 to 0,
        Region.NO5 to 0
    )

    private val temperatureMap: MutableMap<Region, List<Temperature>> = mutableMapOf(
        Region.NO1 to listOf(),
        Region.NO2 to listOf(),
        Region.NO3 to listOf(),
        Region.NO4 to listOf(),
        Region.NO5 to listOf(),
    )

    private val weatherDataMap: MutableMap<Region, WeatherData?> = mutableMapOf(
        Region.NO1 to null,
        Region.NO2 to null,
        Region.NO3 to null,
        Region.NO4 to null,
        Region.NO5 to null,
    )

    suspend fun getToday(region: Region): List<Double> {
        // TODO: fix
        return (0 until 24).fold(listOf()) { acc, _ -> acc + 0.0}
    }

    suspend fun getTomorrow(region: Region): List<Double> {
        // TODO: fix
        return (0 until 24).fold(listOf()) { acc, _ -> acc + 0.0}
    }

    suspend fun getWeek(region: Region): List<Double> {
        // TODO: fix
        return (0 until 7).fold(listOf()) { acc, _ -> acc + 0.0}
    }

    suspend fun getMonth(region: Region): List<Double> {
        // TODO: fix
        return (0 until 30).fold(listOf()) { acc, _ -> acc + 0.0}
    }

    private suspend fun fetchIfNeeded(
        region: Region,
    ) = withContext(Dispatchers.IO) {

        if (weatherDataMap[region] == null) {
            weatherDataMap[region] = fetchWeatherData(region)
            return@withContext
        }
        if ((lastUpdatedMap[region] ?: Long.MAX_VALUE) - getTimeMillis() > 1000 * 60 * 30) {
            weatherDataMap[region] = fetchWeatherData(region)
            return@withContext
        }
    }

    private suspend fun fetchWeatherData(
        region: Region,
        containsTomorrow: Boolean = true
    ): WeatherData? {
        return try {
            TemperatureRepository.getWeatherData(region)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}