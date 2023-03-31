package com.example.stromkalkulator.data.models.electricity

import kotlinx.serialization.Serializable

@Serializable
class Day (val hours: List<HourPrice>)