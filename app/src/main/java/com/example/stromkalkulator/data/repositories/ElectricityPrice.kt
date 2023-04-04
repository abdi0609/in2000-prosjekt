package com.example.stromkalkulator.data.repositories

import com.example.stromkalkulator.data.models.electricity.Day
import com.example.stromkalkulator.data.models.electricity.HourPrice
import com.example.stromkalkulator.data.models.electricity.Week
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.util.Calendar


class ElectricityPrice (private val client: HttpClient) {
    // Denne er visst ikke brukt????
    /*
    private val daysInMonth = hashMapOf(
        "01" to 31, "02" to 28, "03" to 31,
        "04" to 30, "05" to 31, "06" to 30,
        "07" to 31, "08" to 31, "09" to 30,
        "10" to 31, "11" to 30, "12" to 31
    )
    */
    suspend fun getTomorrow(): Day {
        return try {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, 1)
            if (calendar.get(Calendar.HOUR_OF_DAY) < 14) { return Day(emptyList()) }

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            // Denne er visst aldri brukt heller
            //val tomorrow = calendar.time

            var dayString = calendar.get(Calendar.DAY_OF_MONTH).toString()
            var monthString = calendar.get(Calendar.MONTH).toString()
            if (dayString.length == 1) { dayString = "0$dayString" }
            if (monthString.length == 1) { monthString = "0$monthString" }


            val url = "https://www.hvakosterstrommen.no/api/v1/prices/" +
                    calendar.get(Calendar.YEAR).toString() + "/" + monthString + "-" + dayString + "_NO5.json"
            println(url)

            val dayList: List<HourPrice> = client.get(url).body()
            Day(dayList)
        } catch (_: Exception) {
            Day(emptyList())
        }
    }

    suspend fun getToday(): Day {
        return try {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR).toString()

            //get(month) gir dagen før. Vet ikke hvorfor
            var month = (calendar.get(Calendar.MONTH) + 1).toString()
            var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            if (month.length == 1) { month = "0$month" }
            if (day.length == 1) { day = "0$day" }

            val url = "https://www.hvakosterstrommen.no/api/v1/prices/" + year +
                    "/" + month + "-" + day + "_NO5.json"
            println(url)
            val dayList: List<HourPrice> = client.get(url).body()
            Day(dayList)
        } catch (_: Exception) { Day(emptyList()) }
    }

    //converts date-string to HOUR
    private fun getHourFromDate(time: String): Int = time.split("T")[1].subSequence(0,2).toString().toInt()
    suspend fun getCurrent(): String {
        val thisDay = getToday()
        val calendar = Calendar.getInstance()
        val currentTime = calendar.get(Calendar.HOUR_OF_DAY)

        thisDay.hours.forEach {
            val hourStart = getHourFromDate(it.time_start)
            // if this hour
            if (hourStart == currentTime) return it.NOK_per_kWh
        }
        return "Not found"
    }

    suspend fun getWeek(): Week {
        val urlList = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        for (i in 0..6) {
            var month = (calendar.get(Calendar.MONTH) + 1).toString()
            var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            if (month.length == 1) { month = "0$month" }
            if (day.length == 1) { day = "0$day" }

            val url = "https://www.hvakosterstrommen.no/api/v1/prices/" +
                    calendar.get(Calendar.YEAR) + "/" + month + "-" + day + "_NO5.json"
            urlList.add(url)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        val hourPriceList = emptyList<Day>().toMutableList()
        for (url in urlList) {
            val day: Day = client.get(url).body()
            hourPriceList.add(day)
        }

        return Week(hourPriceList)
    }
}