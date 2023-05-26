package com.example.stromkalkulator.data.models

import kotlinx.serialization.Serializable

/*data class Temperature(
    val time: String,
    val value: Double,
)*/

@Serializable
data class WeatherData(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<Float>,
)

@Serializable
data class Properties(
    val meta: Meta,
    val timeseries: List<TimeUnit>
)

@Serializable
data class Meta(
    val updated_at: String,
    val units: Units
)

@Serializable
data class Units(
    val air_pressure_at_sea_level: String,
    val air_temperature: String,
    val cloud_area_fraction: String,
    val precipitation_amount: String,
    val relative_humidity: String,
    val wind_from_direction : String,
    val wind_speed : String
)

@Serializable
data class TimeUnit(
    val time: String,
    val data: Data
)

@Serializable
data class Data(
    val instant: Instant,
    val next_12_hours: NextTwelve? = null,
    val next_1_hours: NextOne? = null,
    val next_6_hours: NextSix? = null,
)

@Serializable
data class Instant(
    val details: FullDetails
)

@Serializable
data class NextTwelve(
    val summary: Summary
)

@Serializable
data class NextOne(
    val summary: Summary,
    val details: SingleDetail
)

@Serializable
data class NextSix(
    val summary: Summary,
    val details: SingleDetail
)

@Serializable
data class SingleDetail(
    val precipitation_amount: String
)

@Serializable
data class FullDetails(
    val air_pressure_at_sea_level: Double,
    val air_temperature: Double,
    val cloud_area_fraction: Double,
    val relative_humidity: Double,
    val wind_from_direction : Double,
    val wind_speed : Double
)

@Serializable
data class Summary(
    val symbol_code: String
)