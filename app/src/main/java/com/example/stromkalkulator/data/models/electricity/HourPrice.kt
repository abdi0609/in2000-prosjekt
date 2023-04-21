package com.example.stromkalkulator.data.models.electricity

import kotlinx.serialization.Serializable

@Serializable
data class HourPrice (
    val NOK_per_kWh: Double,
    val EUR_per_kWh: Double,
    val EXR: Double,
    val time_start: String,
    val time_end: String
)