package com.example.stromkalkulator.data.models

import kotlinx.serialization.Serializable

@Serializable
class Day (val hours: List<HourPrice>)