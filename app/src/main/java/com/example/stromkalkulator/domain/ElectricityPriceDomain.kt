package com.example.stromkalkulator.domain

import android.util.Log
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.models.electricity.HourPrice
import com.example.stromkalkulator.data.repositories.ElectricityPrice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

/**
 * Object responisble for caching the fetched data from "hvakosterstrommen.no"
 *
 * @property lastUpdatedMap Map of last updated time for each region
 * @property regionMap Map of lists of HourPrices for each region
 */
object ElectricityPriceDomain {

    // TODO make tests to make sure list is either empty, or has 31 elements

    private val lastUpdatedMap: MutableMap<Region, Date?> = mutableMapOf(
        Region.NO1 to null,
        Region.NO2 to null,
        Region.NO3 to null,
        Region.NO4 to null,
        Region.NO5 to null
    )

    private val regionMap: MutableMap<Region, List<List<HourPrice>>> = mutableMapOf(
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
            regionMap[region] = listOf()
        }
    }


    /**
     * Gets the electricity price for the current hour
     * Fetches the data from the repository if needed
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    suspend fun getCurrentHour(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): Double {
        fetchIfNeeded(region, calendar)
        return regionMap[region]
            ?.getOrNull(29)
            ?.getOrNull(calendar.get(Calendar.HOUR_OF_DAY)-1)
            ?.NOK_per_kWh
            ?: 0.0
    }

    /**
     * Gets the electricity price for today
     * Fetches the data from the repository if needed
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    suspend fun getToday(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return regionMap[region]
            ?.getOrNull(29)
            ?.map { hp -> hp.NOK_per_kWh }
            ?: listOf()
    }

    /**
     * Gets the electricity price for tomorrow
     * Fetches the data from the repository if needed
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    suspend fun getTomorrow(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return regionMap[region]
            ?.getOrNull(30)
            ?.map { hp -> hp.NOK_per_kWh }
            ?: listOf()
    }

    /**
     * Gets the electricity price for the past week
     * Fetches the data from the repository if needed
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    suspend fun getWeek(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<List<Double>> {
        fetchIfNeeded(region, calendar)
        return regionMap[region]
            ?.subList(30-7,30)
            ?.map { list -> list.map { hp -> hp.NOK_per_kWh } }
            ?: listOf()
    }

    /**
     * Gets the electricity price for the past month
     * Fetches the data from the repository if needed
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    suspend fun getMonth(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<List<Double>> {
        fetchIfNeeded(region, calendar)
        return regionMap[region]
            ?.subList(0,30)
            ?.map { list -> list.map { hp -> hp.NOK_per_kWh } }
            ?: listOf()
    }

    /**
     * Gets the average electricity price for the past week
     * Fetches the data from the repository if needed
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    suspend fun getWeekAverages(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return getWeek(region).map { it.average() }
    }

    /**
     * Gets the average electricity price for the past month
     * Fetches the data from the repository if needed
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    suspend fun getMonthAverages(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return getMonth(region).map { it.average() }
    }

    /**
     * Fetches data from the repository if needed
     *
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     */
    private suspend fun fetchIfNeeded(
        region: Region,
        calendar: Calendar = Calendar.getInstance()
    ) = withContext(Dispatchers.IO) {
        if (regionMap[region]?.isEmpty() == true) {
            lastUpdatedMap[region] = calendar.time
            regionMap[region] = fetchDayRange(29,region)
            return@withContext
        }
        val tomorrowCalendar = calendar.also { it.add(Calendar.HOUR_OF_DAY, 1) }
        if (lastUpdatedMap[region]!! > tomorrowCalendar.time) {
            lastUpdatedMap[region] = calendar.time
            regionMap[region] = fetchDayRange(29, region)
            return@withContext
        }
    }

    /**
     * Fetches data from the repository
     *
     * @param daysBack Number of days to fetch
     * @param region Region to fetch data for
     * @param calendar Calendar to fetch data for
     * @param containsTomorrow Whether the data should contain tomorrow
     */
    private suspend fun fetchDayRange(
        daysBack: Int,
        region: Region,
        calendar: Calendar = Calendar.getInstance(),
        containsTomorrow: Boolean = false
    ): List<List<HourPrice>> {
        Log.v("fetch","Fetchin eprice for $region, $calendar")

        val daysForward = if (containsTomorrow) daysBack+1 else daysBack
        calendar.add(Calendar.DAY_OF_MONTH,-daysBack)
        // TODO: Fixme
        lastUpdatedMap[region] = calendar.time

        return try {
            (0 .. daysForward).fold(emptyList()) { accumulator, _ ->
                (accumulator + listOf(ElectricityPrice.getDay(region, calendar)))
                    .also { calendar.add(Calendar.DAY_OF_MONTH, 1) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            List(30) { emptyList() }
        }
    }

}