package com.example.stromkalkulator.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.stromkalkulator.data.models.Day
import com.example.stromkalkulator.data.models.HourPrice
import com.example.stromkalkulator.data.models.Week
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ElectricityPrice {
    private val daysInMonth = hashMapOf(
        "01" to 31, "02" to 28, "03" to 31,
        "04" to 30, "05" to 31, "06" to 30,
        "07" to 31, "08" to 31, "09" to 30,
        "10" to 31, "11" to 30, "12" to 31
    )
    private val client = HttpClient(CIO) { install(ContentNegotiation) { json() } }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTomorrow(): Day {
        return try {
            val calendar = Calendar.getInstance()
            var dayInt = (calendar.get(Calendar.DAY_OF_MONTH) + 1)
            var dayString = dayInt.toString()
            var monthInt = (calendar.get(Calendar.MONTH) + 1)
            var monthString = monthInt.toString()
            var yearInt = calendar.get(Calendar.YEAR)
            var yearString = yearInt.toString()
            val currentDateTime = LocalDateTime.now()
            val hour = currentDateTime.format(DateTimeFormatter.ofPattern("HH"))
            if (hour.toInt() < 14) { return Day(emptyList()) }

            if ((dayInt - 1) == daysInMonth[monthString]) {
                dayInt = 1
                dayString = dayInt.toString()
                monthInt++
                monthString = monthInt.toString()
                if (monthInt == 13) {
                    monthInt = 1
                    monthString = monthInt.toString()
                    yearInt++
                    yearString = yearInt.toString()
                }
            }
            if (dayString.length == 1) { dayString = "0$dayString" }
            if (monthString.length == 1) { monthString = "0$monthString" }


            val url = "https://www.hvakosterstrommen.no/api/v1/prices/" + yearString + "/" + monthString + "-" + dayString + "_NO5.json"
            println(url)

            val dayList: List<HourPrice> = client.get(url).body<List<HourPrice>>()
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
            if (month.length == 1) { month = "0$month" }
            var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            if (day.length == 1) { day = "0$day" }

            val url = "https://www.hvakosterstrommen.no/api/v1/prices/" + year + "/" + month + "-" + day + "_NO5.json"
            println(url)
            val dayList: List<HourPrice> = client.get(url).body<List<HourPrice>>()
            Day(dayList)
        } catch (_: Exception) {
             Day(emptyList()) }
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
        val calendar = Calendar.getInstance()
        var dayInt = (calendar.get(Calendar.DAY_OF_MONTH) + 1)
        var dayString = dayInt.toString()
        var monthInt = (calendar.get(Calendar.MONTH) + 1)
        var monthString = monthInt.toString()
        var yearInt = calendar.get(Calendar.YEAR)
        var yearString = yearInt.toString()

        val urlList = mutableListOf<String>()
        for (i in 0..5) {
            dayInt--
            dayString = dayInt.toString()
            if (dayInt == 0) {
                monthInt--
                monthString = monthInt.toString()
                if (monthInt == 0) {
                    monthInt = 12
                    monthString = monthInt.toString()
                    yearInt--
                    yearString = yearInt.toString()

                }
                if (monthString.length == 1) { monthString = "0$monthString" }
                dayInt = daysInMonth[monthString]!!
            }
            if (dayString.length == 1) { dayString = "0$dayString" }
            if (monthString.length == 1) { monthString = "0$monthString" }

            val url = "https://www.hvakosterstrommen.no/api/v1/prices/" + yearString + "/" + monthString + "-" + dayString + "_NO5.json"
            urlList.add(url)
        }

        val hourPriceList = emptyList<Day>().toMutableList()
        for (url in urlList) {
            val day: Day = client.get(url).body()
            hourPriceList.add(day)
        }

        return Week(hourPriceList)
    }
}