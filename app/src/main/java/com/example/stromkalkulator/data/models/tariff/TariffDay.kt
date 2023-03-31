package com.example.stromkalkulator.data.models.tariff

import kotlinx.serialization.Serializable

@Serializable
class TariffDay (
    val tariffdato: String,
    val tariffmodell: String,
    val tariffgruppe: String,
    val konsesjonar: String,
    val organisasjonsnr: String,
    val fylkeNr: String,
    val fylkeNavn: String,
    val harMva: Boolean,
    val harForbruksavgift: Boolean,
    val periodeFraDato: String,
    val periodeTilDato: String,
    val fastleddEks: String,
    val energileddEks: String,
    val effektleddKrMndEks: String,
    val effektTrinnFraKw: String,
    val effektTrinnTilKw: String,
    val omregnetAarEks: String,
    val omregnetOreEks: String,
    val fastleddInkMva: String,
    val energileddInkMva: String,
    val effektleddInk: String,
    val omregnetAarInk: String,
    val omregnetOreInk: String,
    val kvantumAar: String
        )