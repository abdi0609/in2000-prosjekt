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
import kotlin.jvm.Throws


@Suppress("SameReturnValue")
object ElectricityPrice {
    private val client: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }

    /**
     * @throws NoTransformationFoundException
     * @throws DoubleReceiveException
     */
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
}
