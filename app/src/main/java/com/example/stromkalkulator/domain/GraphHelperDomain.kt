package com.example.stromkalkulator.domain

import com.example.stromkalkulator.data.Region
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.FloatEntry

object GraphHelperDomain {

    suspend fun getPresentableTomorrowPair(
        region: Region
    ): Pair<List<ChartEntry>, List<ChartEntry>> {
        val prices = ElectricityPriceDomain.getTomorrow(region)
        val temps = TemperatureDomain.getTomorrow(region)
        return listsToChartListPair(prices, temps)
    }

    suspend fun getPresentableWeekPair(
        region: Region
    ): Pair<List<ChartEntry>, List<ChartEntry>> {
        val prices = ElectricityPriceDomain.getWeekAverages(region)
        val temps = TemperatureDomain.getWeek(region)
        return listsToChartListPair(prices, temps)
    }

    suspend fun getPresentableMonthPair(
        region: Region
    ): Pair<List<ChartEntry>, List<ChartEntry>> {
        val prices = ElectricityPriceDomain.getMonthAverages(region)
        val temps = TemperatureDomain.getMonth(region)
        return listsToChartListPair(prices, temps)
    }

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