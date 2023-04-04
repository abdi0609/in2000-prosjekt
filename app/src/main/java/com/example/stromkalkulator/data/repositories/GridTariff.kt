package com.example.stromkalkulator.data.repositories

import com.example.stromkalkulator.data.models.tariff.Country
import com.example.stromkalkulator.data.models.tariff.County
import com.example.stromkalkulator.data.models.tariff.TariffDay
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

class GridTariff (private val client: HttpClient) {

    suspend fun getAvgCounty(fromDate: String, toDate: String): List<County> {
        return try {
            val url = "https://biapi.nve.no/nettleiestatistikk/api/Nettleie/FylkessnittHusholdningFritidsbolig?FraDato=${fromDate}&TilDato=${toDate}"
            client.get(url).body()
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getAvgCountry(fromDate: String, toDate: String): List<Country> {
        return try {
            val url = "https://biapi.nve.no/nettleiestatistikk/api/Nettleie//LandssnittHusholdningFritidsbolig?FraDato=${fromDate}&TilDato=${toDate}"
            client.get(url).body()
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getHistory(fromDate: String, toDate: String, yearlyHousehold: String, effectHousehold: String, yearlyFreetime: String, effectFreetime: String): List<TariffDay> {
        return try {
            val url = "https://biapi.nve.no/nettleiestatistikk/api/Nettleie/HistorikkHusholdningFritid?FraDato=${fromDate}&TilDato=${toDate}&AarligTotalForbrukHusholdning=${yearlyHousehold}&EffektHusholdning=${effectHousehold}&AarligTotalForbrukFritidsbolig=${yearlyFreetime}&EffektFritidsbolig=${effectFreetime}"
            client.get(url).body()
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getDate(date: String, yearlyHousehold: String, effectHousehold: String, yearlyFreetime: String, effectFreetime: String): List<TariffDay> {
        return try {
            val url = "https://biapi.nve.no/nettleiestatistikk/api/Nettleie/ValgtDatoHusholdningFritid?ValgtDato=${date}&AarligTotalForbrukHusholdning=${yearlyHousehold}&EffektHusholdning=${effectHousehold}&AarligTotalForbrukFritidsbolig=${yearlyFreetime}&EffektFritidsbolig=${effectFreetime}"
            client.get(url).body()
        } catch (_: Exception) {
            emptyList()
        }
    }
}