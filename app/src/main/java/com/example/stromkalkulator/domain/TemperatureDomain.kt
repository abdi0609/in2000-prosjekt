package com.example.stromkalkulator.domain

import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.repositories.TemperatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Object responisble for caching the fetched data from the FROST and location forecast APIs
 *
 * @property lastUpdatedMap Map of last updated time for each region
 * @property temperatureMap Map of lists of temperatures for each region
 */
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

    /**
     * Resets the cache
     * Used by unit tests
     */
    fun reset() {
        Region.values().forEach { region ->
            lastUpdatedMap[region] = null
            temperatureMap[region] = listOf()
        }
    }

    /**
     * Gets the temperature for tomorrow as a list of doubles
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    suspend fun getTomorrow(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return temperatureMap[region]
            ?.get(30)
            ?: emptyList()
    }

    /**
     * Gets the temperatures for the past week as a list of doubles
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
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

    /**
     * Gets the temperatures for the past month as a list of doubles
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
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

    /**
     * Fetches the data from the repository if needed
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
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

    /**
     * Fetches the data from the temperature repository
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    private suspend fun fetchWeatherData(
        region: Region,
        calendar: Calendar = Calendar.getInstance(),
    ): List<List<Double>> {
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