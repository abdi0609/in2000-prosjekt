package com.example.stromkalkulator.domain

import android.util.Log
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.repositories.TemperatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

object TemperatureDomain {

    private val lastUpdatedMap: MutableMap<Region, Date?> = mutableMapOf(
        Region.NO1 to null,
        Region.NO2 to null,
        Region.NO3 to null,
        Region.NO4 to null,
        Region.NO5 to null
    )

    private val temperatureMap: MutableMap<Region, List<List<Double>>> = mutableMapOf(
        Region.NO1 to listOf(),
        Region.NO2 to listOf(),
        Region.NO3 to listOf(),
        Region.NO4 to listOf(),
        Region.NO5 to listOf(),
    )

    fun reset() {
        Region.values().forEach { region ->
            lastUpdatedMap[region] = null
            temperatureMap[region] = listOf()
        }
    }

    suspend fun getTomorrow(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return temperatureMap[region]
            ?.get(30)
            ?: emptyList()
    }

    suspend fun getWeek(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return temperatureMap[region]
            ?.subList(30-7,30)
            ?.map { it.average() }
            ?: listOf()
    }

    suspend fun getMonth(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return temperatureMap[region]
            ?.subList(0,30)
            ?.map { it.average() }
            ?: listOf()
    }

    private suspend fun fetchIfNeeded(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ) = withContext(Dispatchers.IO) {

        if (temperatureMap[region] == null) {
            temperatureMap[region] = fetchWeatherData(region)
            return@withContext
        }
        if (lastUpdatedMap[region] == null) {
            temperatureMap[region] = fetchWeatherData(region)
            return@withContext
        }
        val tomorrowCalendar = calendar.also { it.add(Calendar.HOUR_OF_DAY, 1) }
        if (lastUpdatedMap[region]!! > tomorrowCalendar.time) {
            temperatureMap[region] = fetchWeatherData(region)
            return@withContext
        }
    }

    private suspend fun fetchWeatherData(
        region: Region,
        calendar: Calendar = Calendar.getInstance(),
    ): List<List<Double>> {
        Log.v("fetch","Fetchin temp for $region, $calendar")
        lastUpdatedMap[region] = calendar.time
        return try {
            val x = TemperatureRepository.getPast(region,30,1,calendar)
            val y = TemperatureRepository.getTomorrow(region)
            mutableListOf<List<Double>>()
                .also { it.addAll(x) }
                .also { it.add(y)}
        } catch (e: Exception) {
            e.printStackTrace()
            List(30) { emptyList() }
        }
    }
}