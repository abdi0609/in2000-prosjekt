package com.example.stromkalkulator.data.models.tariff

import kotlinx.serialization.Serializable

@Serializable
class Country (
    val tariffdato: String,
    val tariffgruppe: String,
    val omradeNavn: String,
    val omradeNummer: String,
    val sumKvantumAar: String,
    val landssnittEnergiEks: String,
    val landssnittEnergiInk: String,
    val landssnittEffektEks: String,
    val landssnittEffektInk: String,
    val landssnittFastleddEks: String,
    val landssnittFastleddInk: String,
    val landssnittOmregnetOreEks: String,
    val landssnittOmregnetOreInk: String

        )