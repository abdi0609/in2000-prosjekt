package com.example.stromkalkulator.data.models

import kotlinx.serialization.Serializable

@Serializable
class Week (val days: MutableList<Day>)