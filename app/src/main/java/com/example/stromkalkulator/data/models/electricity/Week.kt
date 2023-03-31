package com.example.stromkalkulator.data.models.electricity

import com.example.stromkalkulator.data.models.electricity.Day
import kotlinx.serialization.Serializable

@Serializable
class Week (val days: MutableList<Day>)