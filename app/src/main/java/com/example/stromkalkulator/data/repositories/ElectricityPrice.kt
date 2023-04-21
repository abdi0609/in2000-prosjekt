package com.example.stromkalkulator.data.repositories

import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.models.electricity.Day
import com.example.stromkalkulator.data.models.electricity.HourPrice
import com.example.stromkalkulator.data.models.electricity.Week
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import java.util.Calendar


@Suppress("SameReturnValue")
object ElectricityPrice {
    private val client: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }

    suspend fun getDay(
        region: Region,
        calendar: Calendar = Calendar.getInstance(),
        localClient: HttpClient = client
    ): List<HourPrice> {
        val year = "%02d".format(calendar.get(Calendar.YEAR))
        val month = "%02d".format(calendar.get(Calendar.MONTH) + 1)
        val day = "%02d".format(calendar.get(Calendar.DAY_OF_MONTH))
        val url = "https://www.hvakosterstrommen.no/api/v1/prices/" +
                "${year}/${month}-${day}_${region}.json"

        return localClient.get(url).body()
    }








    suspend fun getTomorrow(region: Region, calendar: Calendar = Calendar.getInstance()): Day {

        if (calendar.get(Calendar.HOUR_OF_DAY) < 14) { return Day(emptyList()) }

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_YEAR, 1)

        return try {

            var dayString = calendar.get(Calendar.DAY_OF_MONTH).toString()
            var monthString = calendar.get(Calendar.MONTH).toString()
            if (dayString.length == 1) { dayString = "0$dayString" }
            if (monthString.length == 1) { monthString = "0$monthString" }


            val url = "https://www.hvakosterstrommen.no/api/v1/prices/" +
                "${calendar.get(Calendar.YEAR)}/${monthString}-${dayString}_${region}.json"

            val dayList: List<HourPrice> = client.get(url).body()
            Day(dayList)
        } catch (_: Exception) {
            Day(emptyList())
        }
    }

    suspend fun getToday(region: Region, calendar: Calendar = Calendar.getInstance()): Day {
        return try {
            val year = calendar.get(Calendar.YEAR).toString()

            //get(month) gir dagen før. Vet ikke hvorfor
            var month = (calendar.get(Calendar.MONTH) + 1).toString()
            var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            if (month.length == 1) { month = "0$month" }
            if (day.length == 1) { day = "0$day" }

            val url = "https://www.hvakosterstrommen.no/api/v1/prices/" +
                    "${year}/${month}-${day}_${region}.json"
            println(url)
            val dayList: List<HourPrice> = client.get(url).body()
            Day(dayList)
        } catch (_: Exception) { Day(emptyList()) }
    }

    //converts date-string to HOUR
    private fun getHourFromDate(time: String):
            Int = time.split("T")[1].subSequence(0,2).toString().toInt()

    suspend fun getCurrent(region: Region, calendar: Calendar = Calendar.getInstance()): Double {
        val thisDay = getToday(region)
        val currentTime = calendar.get(Calendar.HOUR_OF_DAY)

        thisDay.hours.forEach {
            val hourStart = getHourFromDate(it.time_start)
            // if this hour
            if (hourStart == currentTime) return it.NOK_per_kWh
        }
        return 0.0
    }

    suspend fun getWeek(region: Region, calendar: Calendar = Calendar.getInstance()): Week {
        val urlList = mutableListOf<String>()
        for (i in 0..6) {
            var month = (calendar.get(Calendar.MONTH) + 1).toString()
            var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            if (month.length == 1) { month = "0$month" }
            if (day.length == 1) { day = "0$day" }

            val url = "https://www.hvakosterstrommen.no/api/v1/prices/" +
                "${calendar.get(Calendar.YEAR)}/${month}-${day}_${region}.json"
            urlList.add(url)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        val hourPriceList = emptyList<Day>().toMutableList()
        try {
            for (url in urlList) {
                val dayList: List<HourPrice> = client.get(url).body()
                hourPriceList.add(Day(dayList))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Week(hourPriceList)
    }
}
