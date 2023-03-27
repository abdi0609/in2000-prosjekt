package com.example.stromkalkulator.data.models

import kotlinx.serialization.Serializable

@Serializable
data class HourPrice (
    val NOK_per_kWh: String,
    val EUR_per_kWh: String,
    val EXR: String,
    val time_start: String,
    val time_end: String
)