package com.example.stromkalkulator.domain

import com.example.stromkalkulator.data.Region
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.FloatEntry

/**
 * This class is responsible for fetching and formatting data for the graph.
 * It is used by the HomeViewModel to get the data it needs.
 */
object GraphHelperDomain {

    /**
     * Fetches the data for tomorrow and formats it into a pair of lists for the graph.
     *
     * @param region The region to fetch data for.
     */
    suspend fun getPresentableTomorrowPair(
        region: Region
    ): Pair<List<ChartEntry>, List<ChartEntry>> {
        val prices = ElectricityPriceDomain.getTomorrow(region)
        val temps = TemperatureDomain.getTomorrow(region)
        return listsToChartListPair(prices, temps)
    }

    /**
     * Fetches the data for the current week and formats it into a pair of lists for the graph.
     *
     * @param region The region to fetch data for.
     */
    suspend fun getPresentableWeekPair(
        region: Region
    ): Pair<List<ChartEntry>, List<ChartEntry>> {
        val prices = ElectricityPriceDomain.getWeekAverages(region)
        val temps = TemperatureDomain.getWeek(region)
        return listsToChartListPair(prices, temps)
    }

    /**
     * Fetches the data for the current month and formats it into a pair of lists for the graph.
     *
     * @param region The region to fetch data for.
     */
    suspend fun getPresentableMonthPair(
        region: Region
    ): Pair<List<ChartEntry>, List<ChartEntry>> {
        val prices = ElectricityPriceDomain.getMonthAverages(region)
        val temps = TemperatureDomain.getMonth(region)
        return listsToChartListPair(prices, temps)
    }

    /**
     * Converts two lists of doubles into a pair of lists of ChartEntries.
     *
     * @param prices The list of prices.
     * @param temps The list of temperatures.
     */
    private fun listsToChartListPair(
        prices: List<Double>,
        temps: List<Double>
    ): Pair<List<ChartEntry>, List<ChartEntry>> {
        if (prices.size != temps.size) {
            println("Price list and temp list not same size (${prices.size} ${temps.size}) ")
            return Pair(emptyList(), emptyList())
        }
        val ret: Pair<List<ChartEntry>,List<ChartEntry>> = (prices.indices)
            .fold(Pair(mutableListOf(), mutableListOf())) { acc, i ->
                Pair(
                    acc.first + FloatEntry(
                        i.toFloat(),
                        prices[i].toFloat()
                    ),
                    acc.second + FloatEntry(
                        i.toFloat(),
                        temps[i].toFloat()
                    )
                )
            }
        return ret
    }
}