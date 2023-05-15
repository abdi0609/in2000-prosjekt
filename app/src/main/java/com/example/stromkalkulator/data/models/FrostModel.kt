package com.example.stromkalkulator.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FrostData(
    val createdAt: String,
    val data: List<Source>
)

@Serializable
data class Source(
    val sourceId: String,
    val referenceTime: String,
    val observations: List<Observation>
)

@Serializable
data class Observation(
    val elementId: String,
    val value: Double
)
