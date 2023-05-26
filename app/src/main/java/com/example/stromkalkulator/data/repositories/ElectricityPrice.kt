package com.example.stromkalkulator.data.repositories

import android.util.Log
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.models.electricity.HourPrice
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import java.util.Calendar

/**
 * Object responisble for fetching data from "hvakosterstrommen.no"
 *
 * @property client Simple HttpClient setup for fetching json
 */
@Suppress("SameReturnValue")
object ElectricityPrice {
    private val client: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }

    /**
     * Fetches the electricity price for a single day
     *
     * @param region The requested region
     * @param calendar Calendar input to specify requested time
     * @param localClient Optional HttpClient override (used by unit-tests).
     *
     * @return A list of HourPrices representing the price for each hour of the current day
     *
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

        Log.i("ElectricityPrice", "Fetching from $url")
        val response = localClient.get(url)
        Log.i("ElectricityPrice", "Received response $response")

        return response.body()
    }
}
