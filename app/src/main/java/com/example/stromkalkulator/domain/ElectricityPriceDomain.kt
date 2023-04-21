package com.example.stromkalkulator.domain

import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.models.electricity.HourPrice
import com.example.stromkalkulator.data.repositories.ElectricityPrice
import io.ktor.util.date.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

object ElectricityPriceDomain {

    // TODO make tests to make sure list is either empty, or has 31 elements

    private val lastUpdatedMap: MutableMap<Region, Long> = mutableMapOf(
        Region.NO1 to 0,
        Region.NO2 to 0,
        Region.NO3 to 0,
        Region.NO4 to 0,
        Region.NO5 to 0
    )

    private val regionMap: MutableMap<Region, List<List<HourPrice>>> = mutableMapOf(
        Region.NO1 to listOf(),
        Region.NO2 to listOf(),
        Region.NO3 to listOf(),
        Region.NO4 to listOf(),
        Region.NO5 to listOf(),
    )

    suspend fun getToday(
        region: Region = RegionSingleton.region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return regionMap[region]!![29].map { hp -> hp.NOK_per_kWh }
    }

    suspend fun getTomorrow(
        region: Region = RegionSingleton.region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return regionMap[region]!![30].map { hp -> hp.NOK_per_kWh }
    }

    suspend fun getWeek(
        region: Region = RegionSingleton.region,
        calendar: Calendar = Calendar.getInstance()
    ): List<List<Double>> {
        fetchIfNeeded(region, calendar)
        return regionMap[region]!!.subList(30-7,30)
            .map { list -> list.map { hp -> hp.NOK_per_kWh } }
    }

    suspend fun getMonth(
        region: Region = RegionSingleton.region,
        calendar: Calendar = Calendar.getInstance()
    ): List<List<Double>> {
        fetchIfNeeded(region, calendar)
        return regionMap[region]!!.subList(0,30)
            .map { list -> list.map { hp -> hp.NOK_per_kWh } }
    }

    suspend fun getWeekAverages(
        region: Region = RegionSingleton.region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return getWeek().map { it.average() }
    }

    suspend fun getMonthAverages(
        region: Region = RegionSingleton.region,
        calendar: Calendar = Calendar.getInstance()
    ): List<Double> {
        fetchIfNeeded(region, calendar)
        return getMonth().map { it.average() }
    }

    private suspend fun fetchIfNeeded(
        region: Region = RegionSingleton.region,
        calendar: Calendar = Calendar.getInstance()
    ) = withContext(Dispatchers.IO) {

        if (regionMap[region]?.isEmpty() == true) {
            regionMap[region] = fetchDayRange(30,region)
            return@withContext
        }
        if ((lastUpdatedMap[region] ?: Long.MAX_VALUE) - calendar.get(Calendar.MILLISECOND) > 1000 * 60 * 30) {
            regionMap[region] = fetchDayRange(30,region)
            return@withContext
        }
    }

    private suspend fun fetchDayRange(
        daysBack: Int,
        region: Region,
        calendar: Calendar = Calendar.getInstance(),
        containsTomorrow: Boolean = true
    ): List<List<HourPrice>> {

        val daysForward = if (containsTomorrow) daysBack+1 else daysBack
        calendar.add(Calendar.DAY_OF_MONTH,-daysBack)
        lastUpdatedMap[region] = getTimeMillis()

        return try {
            (0 until daysForward).fold(emptyList()) { acc, _ ->
                (acc + listOf( ElectricityPrice.getDay(region, calendar)))
                    .also { calendar.add(Calendar.DAY_OF_MONTH, 1) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}