package com.example.stromkalkulator.data.repositories

import com.example.stromkalkulator.BuildConfig
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.models.FrostData
import com.example.stromkalkulator.data.models.WeatherData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.Calendar

/**
 * Object responisble for fetching data from FROST and location forecast 2.0.
 *
 *
 * @property httpClient Simple HttpClient setup for fetching json
 * @property frostClient Advanced HttpClient setup including the FROST api-key
 */
object TemperatureRepository {
    private val httpClient: HttpClient = HttpClient(CIO) { install(ContentNegotiation) { json() } }
    private val frostClient: HttpClient = HttpClient(CIO){
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(
                        BuildConfig.frostKey,
                        ""
                    )
                }
            }
        }
    }

    private const val baseUrl = "https://api.met.no/weatherapi/locationforecast/2.0/compact"
    private const val frostBase = "https://frost.met.no/observations/v0.jsonld?"
    private const val apiKey = BuildConfig.apiKey

    /**
     * Gets a pair of coordinates near the Region's largest city.
     *
     * @param region The requested region.
     * @return Pair of coordinates for requested region.
     */
    private fun latLongPairFrom(region: Region): Pair<String, String> =
        when (region) {
            Region.NO1 -> "59.91" to "10.75"
            Region.NO2 -> "58.15" to "8.00"
            Region.NO3 -> "63.43" to "10.40"
            Region.NO4 -> "69.65" to "18.96"
            Region.NO5 -> "60.39" to "5.32"
        }

    /**
     * Gets a weather sensor near the Region's largest city.
     *
     * @param region The requested region.
     * @return String representing a sensor.
     */
    private fun sensorFrom(region:Region): String =
        // Hvis disse stasjonene fjernes, funker ikke API-kallene våre
        when (region) {
            Region.NO1 -> "SN18315"
            Region.NO2 -> "SN39200"
            Region.NO3 -> "SN68173"
            Region.NO4 -> "SN90450"
            Region.NO5 -> "SN50539"
        }

    /**
     * Helper function which turns a calendar into a string
     *
     * @param calendar The time to be converted
     * @return A string representing the calendar time
     */
    private fun stringDateFrom(calendar: Calendar = Calendar.getInstance()): String {
        val year = "%02d".format(calendar.get(Calendar.YEAR))
        val month = "%02d".format(calendar.get(Calendar.MONTH) + 1)
        val day = "%02d".format(calendar.get(Calendar.DAY_OF_MONTH))
        return "$year-$month-$day"
    }


    /**
     * Grabs historical weather data from FROST
     *
     * @param region Chosen region
     * @param days How many days to grab
     * @param hourInterval Optional custom hour interval (default: 1)
     * @param calendar Optional time offset.
     * @param localHttpClient Optional custom HttpClient (used by unit-tests)
     *
     * @return A list of lists of doubles represeintf the temperatures for the chosen range
     */
    suspend fun getPast (
        region: Region,
        days: Int,
        hourInterval: Int = 1,
        calendar: Calendar = Calendar.getInstance(),
        localHttpClient: HttpClient = frostClient
    ): List<List<Double>> = withContext(Dispatchers.IO) {

        val fromDate = stringDateFrom(calendar.also { it.add(Calendar.DAY_OF_MONTH, -days) })
        val toDate = stringDateFrom(calendar.also { it.add(Calendar.DAY_OF_MONTH, days) })
        val url = frostBase +
               "sources=${sensorFrom(region)}" +
               "&referencetime=${fromDate}%2F${toDate}" +
               "&elements=air_temperature" +
                "&timeresolutions=PT${hourInterval}H"
        return@withContext try {
            val rawData = localHttpClient.get(url).body<FrostData>()
            val map = mutableMapOf<String, MutableList<Double>>()
            rawData.data.forEach {
                val dayKey = it.referenceTime.split("T")[0]
                map[dayKey]?.add(it.observations[0].value)
                    ?: map.put(dayKey, mutableListOf(it.observations[0].value))
            }
            map.values.toList()
        } catch (e: Exception) {
            List(days) { emptyList()}
        }
    }

    /**
     * Grabs tomorrow's weather forecast from FROST
     *
     * @param region Chosen region
     * @param calendar Optional time offset.
     * @param localHttpClient Optional custom HttpClient (used by unit-tests)
     *
     * @return A list of doubles representing the expected temperatures of tomorrow
     */
    suspend fun getTomorrow(
        region: Region,
        calendar: Calendar = Calendar.getInstance(),
        localHttpClient: HttpClient = httpClient
    ): List<Double> = withContext(Dispatchers.IO) {

        val (lat, lon) = latLongPairFrom(region)
        val url = "$baseUrl?lat=$lat&lon=$lon"
        try {
            val data = localHttpClient.get(url) {
                headers {
                    append(HttpHeaders.Authorization,apiKey)
                }
            }.body<WeatherData>()

            val list = mutableListOf<Double>()
            val tmrwCal = calendar.also { it.add(Calendar.DAY_OF_MONTH,1)}
            data.properties.timeseries.forEach { timeUnit ->
                if (timeUnit.time.split("T")[0] == stringDateFrom(tmrwCal)) {
                    list.add(timeUnit.data.instant.details.air_temperature)
                }
            }

            return@withContext list
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext emptyList()
        }
    }

}