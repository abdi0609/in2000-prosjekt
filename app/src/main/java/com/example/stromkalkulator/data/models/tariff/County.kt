package com.example.stromkalkulator.data.models.tariff

import kotlinx.serialization.Serializable

@Serializable
class County (
    val tariffdato: String,
    val tariffgruppe: String,
    val fylkeNavn: String,
    val fylkeNr: String,
    val kvantumPrFylkePrAr: String,
    val snittEnergiEks: String,
    val snittEnergiInk: String,
    val snittEffektEks: String,
    val snittEffektInk: String,
    val snittFastleddEks: String,
    val snittFastleddInk: String,
    val snittOmregnetOreEks: String,
    val snittOmregnetOreInk: String
)