package com.example.stromkalkulator.data.repositories

import com.example.stromkalkulator.data.Region
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Calendar

/*
internal class ElectricityPriceTest {
    private lateinit var mockedCalendar: Calendar

    @Before
    fun setUp() {
        // Mocks the calendar to return a specific date and time
        mockkStatic(Calendar::class)
        mockedCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 3)
            set(Calendar.DAY_OF_MONTH, 7)
            set(Calendar.DAY_OF_YEAR, 97)
            set(Calendar.HOUR_OF_DAY, 15)
        }
        every { Calendar.getInstance() } returns mockedCalendar
    }

    @Test
    fun getTomorrow() = runBlocking {
        // Sets up a mock client that will return the mock response when the url is requested
        val url = "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-08_NO5.json"
        val mockResponse = """
            [{"NOK_per_kWh":1.2895,"EUR_per_kWh":0.11478,"EXR":11.2345,"time_start":"2023-04-06T00:00:00+02:00","time_end":"2023-04-06T01:00:00+02:00"},
            {"NOK_per_kWh":1.2759,"EUR_per_kWh":0.11357,"EXR":11.2345,"time_start":"2023-04-06T01:00:00+02:00","time_end":"2023-04-06T02:00:00+02:00"},
            {"NOK_per_kWh":1.27512,"EUR_per_kWh":0.1135,"EXR":11.2345,"time_start":"2023-04-06T02:00:00+02:00","time_end":"2023-04-06T03:00:00+02:00"}]
        """.trimIndent()

        val client = createMockClient(url, mockResponse)

        val electricityPrice = ElectricityPrice(client)
        val result = electricityPrice.getTomorrow(Region.NO5, mockedCalendar)

        assertEquals(3, result.hours.size)
        assertEquals(1.2895, result.hours[0].NOK_per_kWh)
        assertEquals(0.11357, result.hours[1].EUR_per_kWh)
        assertEquals("2023-04-06T01:00:00+02:00", result.hours[1].time_start)
        assertEquals(11.2345, result.hours[2].EXR)
        assertEquals("2023-04-06T03:00:00+02:00", result.hours[2].time_end)
    }

    @Test
    fun getTomorrowBefore14() = runBlocking() {
        val calendarMock = mockk<Calendar>()
        every { calendarMock.get(Calendar.HOUR_OF_DAY) } returns 12

        val client = HttpClient(CIO) { install(ContentNegotiation) { json() } }
        val electricityPrice = ElectricityPrice(client)
        val result = electricityPrice.getTomorrow(Region.NO5, calendarMock)

        assertEquals(0, result.hours.size)
    }

    @Test
    fun getTomorrowException() = runBlocking() {
        val client = createMockClient("", "")
        val electricityPrice = ElectricityPrice(client)
        val result = electricityPrice.getTomorrow(Region.NO5, mockedCalendar)

        assertEquals(0, result.hours.size)
    }

    @Test
    fun getToday() = runBlocking() {
        val url1 = "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-07_NO5.json"
        val url2 = "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-07_NO4.json"

        val mockResponse1 = """
            [{"NOK_per_kWh":1.2895,"EUR_per_kWh":0.11478,"EXR":11.2345,"time_start":"2023-04-06T00:00:00+02:00","time_end":"2023-04-06T01:00:00+02:00"},
            {"NOK_per_kWh":1.2759,"EUR_per_kWh":0.11357,"EXR":11.2345,"time_start":"2023-04-06T01:00:00+02:00","time_end":"2023-04-06T02:00:00+02:00"},
            {"NOK_per_kWh":1.27512,"EUR_per_kWh":0.1135,"EXR":11.2345,"time_start":"2023-04-06T02:00:00+02:00","time_end":"2023-04-06T03:00:00+02:00"}]
        """.trimIndent()

        val mockResponse2 = """
            [{"NOK_per_kWh":2.27512,"EUR_per_kWh":0.1135,"EXR":11.2345,"time_start":"2023-04-06T02:00:00+02:00","time_end":"2023-04-06T03:00:00+02:00"}]
        """.trimIndent()

        val mockClient1 = createMockClient(url1, mockResponse1)
        val mockClient2 = createMockClient(url2, mockResponse2)

        val electricityPrice1 = ElectricityPrice(mockClient1)
        val result = electricityPrice1.getToday(Region.NO5, mockedCalendar)

        val electricityPrice2 = ElectricityPrice(mockClient2)
        val result2 = electricityPrice2.getToday(Region.NO4, mockedCalendar)

        assertEquals(3, result.hours.size)
        assertEquals(1.2895, result.hours[0].NOK_per_kWh)
        assertEquals(0.11357, result.hours[1].EUR_per_kWh)
        assertEquals("2023-04-06T01:00:00+02:00", result.hours[1].time_start)
        assertEquals(11.2345, result.hours[2].EXR)
        assertEquals("2023-04-06T03:00:00+02:00", result.hours[2].time_end)

        assertEquals(1, result2.hours.size)
    }

    @Test
    fun getTodayException() = runBlocking() {
        val client = createMockClient("", "")
        val electricityPrice = ElectricityPrice(client)
        val result = electricityPrice.getToday(Region.NO5, mockedCalendar)

        assertEquals(0, result.hours.size)
    }

    @Test
    fun getCurrent() = runBlocking() {
        val url = "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-07_NO5.json"
        val mockResponse = """
            [{"NOK_per_kWh":1.2895,"EUR_per_kWh":0.11478,"EXR":11.2345,"time_start":"2023-04-06T00:00:00+02:00","time_end":"2023-04-06T01:00:00+02:00"},
            {"NOK_per_kWh":1.2759,"EUR_per_kWh":0.11357,"EXR":11.2345,"time_start":"2023-04-06T01:00:00+02:00","time_end":"2023-04-06T02:00:00+02:00"},
            {"NOK_per_kWh":1.27512,"EUR_per_kWh":0.1135,"EXR":11.2345,"time_start":"2023-04-06T02:00:00+02:00","time_end":"2023-04-06T03:00:00+02:00"},
            {"NOK_per_kWh":1.25546,"EUR_per_kWh":0.11175,"EXR":11.2345,"time_start":"2023-04-06T03:00:00+02:00","time_end":"2023-04-06T04:00:00+02:00"},
            {"NOK_per_kWh":1.25029,"EUR_per_kWh":0.11129,"EXR":11.2345,"time_start":"2023-04-06T04:00:00+02:00","time_end":"2023-04-06T05:00:00+02:00"},
            {"NOK_per_kWh":1.29073,"EUR_per_kWh":0.11489,"EXR":11.2345,"time_start":"2023-04-06T05:00:00+02:00","time_end":"2023-04-06T06:00:00+02:00"},
            {"NOK_per_kWh":1.28983,"EUR_per_kWh":0.11481,"EXR":11.2345,"time_start":"2023-04-06T06:00:00+02:00","time_end":"2023-04-06T07:00:00+02:00"},
            {"NOK_per_kWh":1.32163,"EUR_per_kWh":0.11764,"EXR":11.2345,"time_start":"2023-04-06T07:00:00+02:00","time_end":"2023-04-06T08:00:00+02:00"},
            {"NOK_per_kWh":1.32623,"EUR_per_kWh":0.11805,"EXR":11.2345,"time_start":"2023-04-06T08:00:00+02:00","time_end":"2023-04-06T09:00:00+02:00"},
            {"NOK_per_kWh":1.31781,"EUR_per_kWh":0.1173,"EXR":11.2345,"time_start":"2023-04-06T09:00:00+02:00","time_end":"2023-04-06T10:00:00+02:00"},
            {"NOK_per_kWh":1.25692,"EUR_per_kWh":0.11188,"EXR":11.2345,"time_start":"2023-04-06T10:00:00+02:00","time_end":"2023-04-06T11:00:00+02:00"},
            {"NOK_per_kWh":1.20782,"EUR_per_kWh":0.10751,"EXR":11.2345,"time_start":"2023-04-06T11:00:00+02:00","time_end":"2023-04-06T12:00:00+02:00"},
            {"NOK_per_kWh":1.16861,"EUR_per_kWh":0.10402,"EXR":11.2345,"time_start":"2023-04-06T12:00:00+02:00","time_end":"2023-04-06T13:00:00+02:00"},
            {"NOK_per_kWh":1.15996,"EUR_per_kWh":0.10325,"EXR":11.2345,"time_start":"2023-04-06T13:00:00+02:00","time_end":"2023-04-06T14:00:00+02:00"},
            {"NOK_per_kWh":1.17086,"EUR_per_kWh":0.10422,"EXR":11.2345,"time_start":"2023-04-06T14:00:00+02:00","time_end":"2023-04-06T15:00:00+02:00"},
            {"NOK_per_kWh":1.17592,"EUR_per_kWh":0.10467,"EXR":11.2345,"time_start":"2023-04-06T15:00:00+02:00","time_end":"2023-04-06T16:00:00+02:00"},
            {"NOK_per_kWh":1.19614,"EUR_per_kWh":0.10647,"EXR":11.2345,"time_start":"2023-04-06T16:00:00+02:00","time_end":"2023-04-06T17:00:00+02:00"}]
        """.trimIndent()

        val mockClient = createMockClient(url, mockResponse)

        val electricityPrice = ElectricityPrice(mockClient)
        val result = electricityPrice.getCurrent(Region.NO5, mockedCalendar)

        assertEquals(1.17592, result)
    }

    @Test
    fun getCurrentNotFound() = runBlocking() {
        val url = "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-01_NO5.json"
        val mockResponse = """[]""".trimIndent()

        val mockClient = createMockClient(url, mockResponse)

        val electricityPrice = ElectricityPrice(mockClient)
        val result = electricityPrice.getCurrent(Region.NO5, mockedCalendar)

        assertEquals(0.0, result)
    }

    @Test
    fun getWeek() = runBlocking() {
        val urls = listOf(
            "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-01_NO5.json",
            "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-02_NO5.json",
            "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-03_NO5.json",
            "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-04_NO5.json",
            "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-05_NO5.json",
            "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-06_NO5.json",
            "https://www.hvakosterstrommen.no/api/v1/prices/2023/04-07_NO5.json"
        )
        val responses = listOf(
            """[{"NOK_per_kWh":0.96432,"EUR_per_kWh":0.08491,"EXR":11.357,"time_start":"2023-04-01T00:00:00+02:00","time_end":"2023-04-01T01:00:00+02:00"},{"NOK_per_kWh":0.92832,"EUR_per_kWh":0.08174,"EXR":11.357,"time_start":"2023-04-01T01:00:00+02:00","time_end":"2023-04-01T02:00:00+02:00"},{"NOK_per_kWh":0.92151,"EUR_per_kWh":0.08114,"EXR":11.357,"time_start":"2023-04-01T02:00:00+02:00","time_end":"2023-04-01T03:00:00+02:00"},{"NOK_per_kWh":0.92298,"EUR_per_kWh":0.08127,"EXR":11.357,"time_start":"2023-04-01T03:00:00+02:00","time_end":"2023-04-01T04:00:00+02:00"},{"NOK_per_kWh":0.934,"EUR_per_kWh":0.08224,"EXR":11.357,"time_start":"2023-04-01T04:00:00+02:00","time_end":"2023-04-01T05:00:00+02:00"},{"NOK_per_kWh":0.94297,"EUR_per_kWh":0.08303,"EXR":11.357,"time_start":"2023-04-01T05:00:00+02:00","time_end":"2023-04-01T06:00:00+02:00"},{"NOK_per_kWh":0.95955,"EUR_per_kWh":0.08449,"EXR":11.357,"time_start":"2023-04-01T06:00:00+02:00","time_end":"2023-04-01T07:00:00+02:00"},{"NOK_per_kWh":0.98318,"EUR_per_kWh":0.08657,"EXR":11.357,"time_start":"2023-04-01T07:00:00+02:00","time_end":"2023-04-01T08:00:00+02:00"},{"NOK_per_kWh":1.01009,"EUR_per_kWh":0.08894,"EXR":11.357,"time_start":"2023-04-01T08:00:00+02:00","time_end":"2023-04-01T09:00:00+02:00"},{"NOK_per_kWh":1.01304,"EUR_per_kWh":0.0892,"EXR":11.357,"time_start":"2023-04-01T09:00:00+02:00","time_end":"2023-04-01T10:00:00+02:00"},{"NOK_per_kWh":1.01134,"EUR_per_kWh":0.08905,"EXR":11.357,"time_start":"2023-04-01T10:00:00+02:00","time_end":"2023-04-01T11:00:00+02:00"},{"NOK_per_kWh":0.98988,"EUR_per_kWh":0.08716,"EXR":11.357,"time_start":"2023-04-01T11:00:00+02:00","time_end":"2023-04-01T12:00:00+02:00"},{"NOK_per_kWh":0.97364,"EUR_per_kWh":0.08573,"EXR":11.357,"time_start":"2023-04-01T12:00:00+02:00","time_end":"2023-04-01T13:00:00+02:00"},{"NOK_per_kWh":0.96989,"EUR_per_kWh":0.0854,"EXR":11.357,"time_start":"2023-04-01T13:00:00+02:00","time_end":"2023-04-01T14:00:00+02:00"},{"NOK_per_kWh":0.96307,"EUR_per_kWh":0.0848,"EXR":11.357,"time_start":"2023-04-01T14:00:00+02:00","time_end":"2023-04-01T15:00:00+02:00"},{"NOK_per_kWh":0.96194,"EUR_per_kWh":0.0847,"EXR":11.357,"time_start":"2023-04-01T15:00:00+02:00","time_end":"2023-04-01T16:00:00+02:00"},{"NOK_per_kWh":0.96182,"EUR_per_kWh":0.08469,"EXR":11.357,"time_start":"2023-04-01T16:00:00+02:00","time_end":"2023-04-01T17:00:00+02:00"},{"NOK_per_kWh":0.98011,"EUR_per_kWh":0.0863,"EXR":11.357,"time_start":"2023-04-01T17:00:00+02:00","time_end":"2023-04-01T18:00:00+02:00"},{"NOK_per_kWh":1.00282,"EUR_per_kWh":0.0883,"EXR":11.357,"time_start":"2023-04-01T18:00:00+02:00","time_end":"2023-04-01T19:00:00+02:00"},{"NOK_per_kWh":1.06313,"EUR_per_kWh":0.09361,"EXR":11.357,"time_start":"2023-04-01T19:00:00+02:00","time_end":"2023-04-01T20:00:00+02:00"},{"NOK_per_kWh":1.01952,"EUR_per_kWh":0.08977,"EXR":11.357,"time_start":"2023-04-01T20:00:00+02:00","time_end":"2023-04-01T21:00:00+02:00"},{"NOK_per_kWh":1.011,"EUR_per_kWh":0.08902,"EXR":11.357,"time_start":"2023-04-01T21:00:00+02:00","time_end":"2023-04-01T22:00:00+02:00"},{"NOK_per_kWh":0.99385,"EUR_per_kWh":0.08751,"EXR":11.357,"time_start":"2023-04-01T22:00:00+02:00","time_end":"2023-04-01T23:00:00+02:00"},{"NOK_per_kWh":0.97159,"EUR_per_kWh":0.08555,"EXR":11.357,"time_start":"2023-04-01T23:00:00+02:00","time_end":"2023-04-02T00:00:00+02:00"}]""",
            """[{"NOK_per_kWh":0.99903,"EUR_per_kWh":0.08768,"EXR":11.394,"time_start":"2023-04-02T00:00:00+02:00","time_end":"2023-04-02T01:00:00+02:00"},{"NOK_per_kWh":0.99082,"EUR_per_kWh":0.08696,"EXR":11.394,"time_start":"2023-04-02T01:00:00+02:00","time_end":"2023-04-02T02:00:00+02:00"},{"NOK_per_kWh":0.9849,"EUR_per_kWh":0.08644,"EXR":11.394,"time_start":"2023-04-02T02:00:00+02:00","time_end":"2023-04-02T03:00:00+02:00"},{"NOK_per_kWh":0.98513,"EUR_per_kWh":0.08646,"EXR":11.394,"time_start":"2023-04-02T03:00:00+02:00","time_end":"2023-04-02T04:00:00+02:00"},{"NOK_per_kWh":0.98889,"EUR_per_kWh":0.08679,"EXR":11.394,"time_start":"2023-04-02T04:00:00+02:00","time_end":"2023-04-02T05:00:00+02:00"},{"NOK_per_kWh":0.99219,"EUR_per_kWh":0.08708,"EXR":11.394,"time_start":"2023-04-02T05:00:00+02:00","time_end":"2023-04-02T06:00:00+02:00"},{"NOK_per_kWh":1.00051,"EUR_per_kWh":0.08781,"EXR":11.394,"time_start":"2023-04-02T06:00:00+02:00","time_end":"2023-04-02T07:00:00+02:00"},{"NOK_per_kWh":1.00689,"EUR_per_kWh":0.08837,"EXR":11.394,"time_start":"2023-04-02T07:00:00+02:00","time_end":"2023-04-02T08:00:00+02:00"},{"NOK_per_kWh":1.01247,"EUR_per_kWh":0.08886,"EXR":11.394,"time_start":"2023-04-02T08:00:00+02:00","time_end":"2023-04-02T09:00:00+02:00"},{"NOK_per_kWh":1.0217,"EUR_per_kWh":0.08967,"EXR":11.394,"time_start":"2023-04-02T09:00:00+02:00","time_end":"2023-04-02T10:00:00+02:00"},{"NOK_per_kWh":1.02227,"EUR_per_kWh":0.08972,"EXR":11.394,"time_start":"2023-04-02T10:00:00+02:00","time_end":"2023-04-02T11:00:00+02:00"},{"NOK_per_kWh":1.01498,"EUR_per_kWh":0.08908,"EXR":11.394,"time_start":"2023-04-02T11:00:00+02:00","time_end":"2023-04-02T12:00:00+02:00"},{"NOK_per_kWh":1.00575,"EUR_per_kWh":0.08827,"EXR":11.394,"time_start":"2023-04-02T12:00:00+02:00","time_end":"2023-04-02T13:00:00+02:00"},{"NOK_per_kWh":0.99378,"EUR_per_kWh":0.08722,"EXR":11.394,"time_start":"2023-04-02T13:00:00+02:00","time_end":"2023-04-02T14:00:00+02:00"},{"NOK_per_kWh":0.98467,"EUR_per_kWh":0.08642,"EXR":11.394,"time_start":"2023-04-02T14:00:00+02:00","time_end":"2023-04-02T15:00:00+02:00"},{"NOK_per_kWh":0.97817,"EUR_per_kWh":0.08585,"EXR":11.394,"time_start":"2023-04-02T15:00:00+02:00","time_end":"2023-04-02T16:00:00+02:00"},{"NOK_per_kWh":0.97145,"EUR_per_kWh":0.08526,"EXR":11.394,"time_start":"2023-04-02T16:00:00+02:00","time_end":"2023-04-02T17:00:00+02:00"},{"NOK_per_kWh":1.03788,"EUR_per_kWh":0.09109,"EXR":11.394,"time_start":"2023-04-02T17:00:00+02:00","time_end":"2023-04-02T18:00:00+02:00"},{"NOK_per_kWh":1.10636,"EUR_per_kWh":0.0971,"EXR":11.394,"time_start":"2023-04-02T18:00:00+02:00","time_end":"2023-04-02T19:00:00+02:00"},{"NOK_per_kWh":1.18999,"EUR_per_kWh":0.10444,"EXR":11.394,"time_start":"2023-04-02T19:00:00+02:00","time_end":"2023-04-02T20:00:00+02:00"},{"NOK_per_kWh":1.2244,"EUR_per_kWh":0.10746,"EXR":11.394,"time_start":"2023-04-02T20:00:00+02:00","time_end":"2023-04-02T21:00:00+02:00"},{"NOK_per_kWh":1.27863,"EUR_per_kWh":0.11222,"EXR":11.394,"time_start":"2023-04-02T21:00:00+02:00","time_end":"2023-04-02T22:00:00+02:00"},{"NOK_per_kWh":1.24662,"EUR_per_kWh":0.10941,"EXR":11.394,"time_start":"2023-04-02T22:00:00+02:00","time_end":"2023-04-02T23:00:00+02:00"},{"NOK_per_kWh":1.20731,"EUR_per_kWh":0.10596,"EXR":11.394,"time_start":"2023-04-02T23:00:00+02:00","time_end":"2023-04-03T00:00:00+02:00"}]""",
            """[{"NOK_per_kWh":1.2007,"EUR_per_kWh":0.10538,"EXR":11.394,"time_start":"2023-04-03T00:00:00+02:00","time_end":"2023-04-03T01:00:00+02:00"},{"NOK_per_kWh":1.19136,"EUR_per_kWh":0.10456,"EXR":11.394,"time_start":"2023-04-03T01:00:00+02:00","time_end":"2023-04-03T02:00:00+02:00"},{"NOK_per_kWh":1.19284,"EUR_per_kWh":0.10469,"EXR":11.394,"time_start":"2023-04-03T02:00:00+02:00","time_end":"2023-04-03T03:00:00+02:00"},{"NOK_per_kWh":1.17814,"EUR_per_kWh":0.1034,"EXR":11.394,"time_start":"2023-04-03T03:00:00+02:00","time_end":"2023-04-03T04:00:00+02:00"},{"NOK_per_kWh":1.1443,"EUR_per_kWh":0.10043,"EXR":11.394,"time_start":"2023-04-03T04:00:00+02:00","time_end":"2023-04-03T05:00:00+02:00"},{"NOK_per_kWh":1.19637,"EUR_per_kWh":0.105,"EXR":11.394,"time_start":"2023-04-03T05:00:00+02:00","time_end":"2023-04-03T06:00:00+02:00"},{"NOK_per_kWh":1.52953,"EUR_per_kWh":0.13424,"EXR":11.394,"time_start":"2023-04-03T06:00:00+02:00","time_end":"2023-04-03T07:00:00+02:00"},{"NOK_per_kWh":1.85734,"EUR_per_kWh":0.16301,"EXR":11.394,"time_start":"2023-04-03T07:00:00+02:00","time_end":"2023-04-03T08:00:00+02:00"},{"NOK_per_kWh":1.80846,"EUR_per_kWh":0.15872,"EXR":11.394,"time_start":"2023-04-03T08:00:00+02:00","time_end":"2023-04-03T09:00:00+02:00"},{"NOK_per_kWh":1.27442,"EUR_per_kWh":0.11185,"EXR":11.394,"time_start":"2023-04-03T09:00:00+02:00","time_end":"2023-04-03T10:00:00+02:00"},{"NOK_per_kWh":1.1852,"EUR_per_kWh":0.10402,"EXR":11.394,"time_start":"2023-04-03T10:00:00+02:00","time_end":"2023-04-03T11:00:00+02:00"},{"NOK_per_kWh":1.08471,"EUR_per_kWh":0.0952,"EXR":11.394,"time_start":"2023-04-03T11:00:00+02:00","time_end":"2023-04-03T12:00:00+02:00"},{"NOK_per_kWh":1.01498,"EUR_per_kWh":0.08908,"EXR":11.394,"time_start":"2023-04-03T12:00:00+02:00","time_end":"2023-04-03T13:00:00+02:00"},{"NOK_per_kWh":0.95254,"EUR_per_kWh":0.0836,"EXR":11.394,"time_start":"2023-04-03T13:00:00+02:00","time_end":"2023-04-03T14:00:00+02:00"},{"NOK_per_kWh":0.91733,"EUR_per_kWh":0.08051,"EXR":11.394,"time_start":"2023-04-03T14:00:00+02:00","time_end":"2023-04-03T15:00:00+02:00"},{"NOK_per_kWh":0.99094,"EUR_per_kWh":0.08697,"EXR":11.394,"time_start":"2023-04-03T15:00:00+02:00","time_end":"2023-04-03T16:00:00+02:00"},{"NOK_per_kWh":1.04016,"EUR_per_kWh":0.09129,"EXR":11.394,"time_start":"2023-04-03T16:00:00+02:00","time_end":"2023-04-03T17:00:00+02:00"},{"NOK_per_kWh":1.14419,"EUR_per_kWh":0.10042,"EXR":11.394,"time_start":"2023-04-03T17:00:00+02:00","time_end":"2023-04-03T18:00:00+02:00"},{"NOK_per_kWh":1.22691,"EUR_per_kWh":0.10768,"EXR":11.394,"time_start":"2023-04-03T18:00:00+02:00","time_end":"2023-04-03T19:00:00+02:00"},{"NOK_per_kWh":1.69793,"EUR_per_kWh":0.14902,"EXR":11.394,"time_start":"2023-04-03T19:00:00+02:00","time_end":"2023-04-03T20:00:00+02:00"},{"NOK_per_kWh":1.72505,"EUR_per_kWh":0.1514,"EXR":11.394,"time_start":"2023-04-03T20:00:00+02:00","time_end":"2023-04-03T21:00:00+02:00"},{"NOK_per_kWh":1.51893,"EUR_per_kWh":0.13331,"EXR":11.394,"time_start":"2023-04-03T21:00:00+02:00","time_end":"2023-04-03T22:00:00+02:00"},{"NOK_per_kWh":1.28433,"EUR_per_kWh":0.11272,"EXR":11.394,"time_start":"2023-04-03T22:00:00+02:00","time_end":"2023-04-03T23:00:00+02:00"},{"NOK_per_kWh":1.24502,"EUR_per_kWh":0.10927,"EXR":11.394,"time_start":"2023-04-03T23:00:00+02:00","time_end":"2023-04-04T00:00:00+02:00"}]""",
            """[{"NOK_per_kWh":1.22383,"EUR_per_kWh":0.10741,"EXR":11.394,"time_start":"2023-04-04T00:00:00+02:00","time_end":"2023-04-04T01:00:00+02:00"},{"NOK_per_kWh":1.21471,"EUR_per_kWh":0.10661,"EXR":11.394,"time_start":"2023-04-04T01:00:00+02:00","time_end":"2023-04-04T02:00:00+02:00"},{"NOK_per_kWh":1.22782,"EUR_per_kWh":0.10776,"EXR":11.394,"time_start":"2023-04-04T02:00:00+02:00","time_end":"2023-04-04T03:00:00+02:00"},{"NOK_per_kWh":1.22759,"EUR_per_kWh":0.10774,"EXR":11.394,"time_start":"2023-04-04T03:00:00+02:00","time_end":"2023-04-04T04:00:00+02:00"},{"NOK_per_kWh":1.22782,"EUR_per_kWh":0.10776,"EXR":11.394,"time_start":"2023-04-04T04:00:00+02:00","time_end":"2023-04-04T05:00:00+02:00"},{"NOK_per_kWh":1.25288,"EUR_per_kWh":0.10996,"EXR":11.394,"time_start":"2023-04-04T05:00:00+02:00","time_end":"2023-04-04T06:00:00+02:00"},{"NOK_per_kWh":1.34461,"EUR_per_kWh":0.11801,"EXR":11.394,"time_start":"2023-04-04T06:00:00+02:00","time_end":"2023-04-04T07:00:00+02:00"},{"NOK_per_kWh":1.99156,"EUR_per_kWh":0.17479,"EXR":11.394,"time_start":"2023-04-04T07:00:00+02:00","time_end":"2023-04-04T08:00:00+02:00"},{"NOK_per_kWh":1.97116,"EUR_per_kWh":0.173,"EXR":11.394,"time_start":"2023-04-04T08:00:00+02:00","time_end":"2023-04-04T09:00:00+02:00"},{"NOK_per_kWh":1.54172,"EUR_per_kWh":0.13531,"EXR":11.394,"time_start":"2023-04-04T09:00:00+02:00","time_end":"2023-04-04T10:00:00+02:00"},{"NOK_per_kWh":1.31703,"EUR_per_kWh":0.11559,"EXR":11.394,"time_start":"2023-04-04T10:00:00+02:00","time_end":"2023-04-04T11:00:00+02:00"},{"NOK_per_kWh":1.2416,"EUR_per_kWh":0.10897,"EXR":11.394,"time_start":"2023-04-04T11:00:00+02:00","time_end":"2023-04-04T12:00:00+02:00"},{"NOK_per_kWh":1.21164,"EUR_per_kWh":0.10634,"EXR":11.394,"time_start":"2023-04-04T12:00:00+02:00","time_end":"2023-04-04T13:00:00+02:00"},{"NOK_per_kWh":1.20229,"EUR_per_kWh":0.10552,"EXR":11.394,"time_start":"2023-04-04T13:00:00+02:00","time_end":"2023-04-04T14:00:00+02:00"},{"NOK_per_kWh":1.19341,"EUR_per_kWh":0.10474,"EXR":11.394,"time_start":"2023-04-04T14:00:00+02:00","time_end":"2023-04-04T15:00:00+02:00"},{"NOK_per_kWh":1.2089,"EUR_per_kWh":0.1061,"EXR":11.394,"time_start":"2023-04-04T15:00:00+02:00","time_end":"2023-04-04T16:00:00+02:00"},{"NOK_per_kWh":1.24035,"EUR_per_kWh":0.10886,"EXR":11.394,"time_start":"2023-04-04T16:00:00+02:00","time_end":"2023-04-04T17:00:00+02:00"},{"NOK_per_kWh":1.32273,"EUR_per_kWh":0.11609,"EXR":11.394,"time_start":"2023-04-04T17:00:00+02:00","time_end":"2023-04-04T18:00:00+02:00"},{"NOK_per_kWh":1.35611,"EUR_per_kWh":0.11902,"EXR":11.394,"time_start":"2023-04-04T18:00:00+02:00","time_end":"2023-04-04T19:00:00+02:00"},{"NOK_per_kWh":1.35999,"EUR_per_kWh":0.11936,"EXR":11.394,"time_start":"2023-04-04T19:00:00+02:00","time_end":"2023-04-04T20:00:00+02:00"},{"NOK_per_kWh":1.44738,"EUR_per_kWh":0.12703,"EXR":11.394,"time_start":"2023-04-04T20:00:00+02:00","time_end":"2023-04-04T21:00:00+02:00"},{"NOK_per_kWh":1.37651,"EUR_per_kWh":0.12081,"EXR":11.394,"time_start":"2023-04-04T21:00:00+02:00","time_end":"2023-04-04T22:00:00+02:00"},{"NOK_per_kWh":1.30325,"EUR_per_kWh":0.11438,"EXR":11.394,"time_start":"2023-04-04T22:00:00+02:00","time_end":"2023-04-04T23:00:00+02:00"},{"NOK_per_kWh":1.25152,"EUR_per_kWh":0.10984,"EXR":11.394,"time_start":"2023-04-04T23:00:00+02:00","time_end":"2023-04-05T00:00:00+02:00"}]""",
            """[{"NOK_per_kWh":1.2344,"EUR_per_kWh":0.10971,"EXR":11.2515,"time_start":"2023-04-05T00:00:00+02:00","time_end":"2023-04-05T01:00:00+02:00"},{"NOK_per_kWh":1.22855,"EUR_per_kWh":0.10919,"EXR":11.2515,"time_start":"2023-04-05T01:00:00+02:00","time_end":"2023-04-05T02:00:00+02:00"},{"NOK_per_kWh":1.22619,"EUR_per_kWh":0.10898,"EXR":11.2515,"time_start":"2023-04-05T02:00:00+02:00","time_end":"2023-04-05T03:00:00+02:00"},{"NOK_per_kWh":1.22664,"EUR_per_kWh":0.10902,"EXR":11.2515,"time_start":"2023-04-05T03:00:00+02:00","time_end":"2023-04-05T04:00:00+02:00"},{"NOK_per_kWh":1.23181,"EUR_per_kWh":0.10948,"EXR":11.2515,"time_start":"2023-04-05T04:00:00+02:00","time_end":"2023-04-05T05:00:00+02:00"},{"NOK_per_kWh":1.27446,"EUR_per_kWh":0.11327,"EXR":11.2515,"time_start":"2023-04-05T05:00:00+02:00","time_end":"2023-04-05T06:00:00+02:00"},{"NOK_per_kWh":1.32981,"EUR_per_kWh":0.11819,"EXR":11.2515,"time_start":"2023-04-05T06:00:00+02:00","time_end":"2023-04-05T07:00:00+02:00"},{"NOK_per_kWh":1.68773,"EUR_per_kWh":0.15,"EXR":11.2515,"time_start":"2023-04-05T07:00:00+02:00","time_end":"2023-04-05T08:00:00+02:00"},{"NOK_per_kWh":1.92941,"EUR_per_kWh":0.17148,"EXR":11.2515,"time_start":"2023-04-05T08:00:00+02:00","time_end":"2023-04-05T09:00:00+02:00"},{"NOK_per_kWh":1.48992,"EUR_per_kWh":0.13242,"EXR":11.2515,"time_start":"2023-04-05T09:00:00+02:00","time_end":"2023-04-05T10:00:00+02:00"},{"NOK_per_kWh":1.30394,"EUR_per_kWh":0.11589,"EXR":11.2515,"time_start":"2023-04-05T10:00:00+02:00","time_end":"2023-04-05T11:00:00+02:00"},{"NOK_per_kWh":1.25375,"EUR_per_kWh":0.11143,"EXR":11.2515,"time_start":"2023-04-05T11:00:00+02:00","time_end":"2023-04-05T12:00:00+02:00"},{"NOK_per_kWh":1.23845,"EUR_per_kWh":0.11007,"EXR":11.2515,"time_start":"2023-04-05T12:00:00+02:00","time_end":"2023-04-05T13:00:00+02:00"},{"NOK_per_kWh":1.24115,"EUR_per_kWh":0.11031,"EXR":11.2515,"time_start":"2023-04-05T13:00:00+02:00","time_end":"2023-04-05T14:00:00+02:00"},{"NOK_per_kWh":1.23699,"EUR_per_kWh":0.10994,"EXR":11.2515,"time_start":"2023-04-05T14:00:00+02:00","time_end":"2023-04-05T15:00:00+02:00"},{"NOK_per_kWh":1.23699,"EUR_per_kWh":0.10994,"EXR":11.2515,"time_start":"2023-04-05T15:00:00+02:00","time_end":"2023-04-05T16:00:00+02:00"},{"NOK_per_kWh":1.23812,"EUR_per_kWh":0.11004,"EXR":11.2515,"time_start":"2023-04-05T16:00:00+02:00","time_end":"2023-04-05T17:00:00+02:00"},{"NOK_per_kWh":1.25409,"EUR_per_kWh":0.11146,"EXR":11.2515,"time_start":"2023-04-05T17:00:00+02:00","time_end":"2023-04-05T18:00:00+02:00"},{"NOK_per_kWh":1.25533,"EUR_per_kWh":0.11157,"EXR":11.2515,"time_start":"2023-04-05T18:00:00+02:00","time_end":"2023-04-05T19:00:00+02:00"},{"NOK_per_kWh":1.24138,"EUR_per_kWh":0.11033,"EXR":11.2515,"time_start":"2023-04-05T19:00:00+02:00","time_end":"2023-04-05T20:00:00+02:00"},{"NOK_per_kWh":1.23924,"EUR_per_kWh":0.11014,"EXR":11.2515,"time_start":"2023-04-05T20:00:00+02:00","time_end":"2023-04-05T21:00:00+02:00"},{"NOK_per_kWh":1.2488,"EUR_per_kWh":0.11099,"EXR":11.2515,"time_start":"2023-04-05T21:00:00+02:00","time_end":"2023-04-05T22:00:00+02:00"},{"NOK_per_kWh":1.24993,"EUR_per_kWh":0.11109,"EXR":11.2515,"time_start":"2023-04-05T22:00:00+02:00","time_end":"2023-04-05T23:00:00+02:00"},{"NOK_per_kWh":1.23598,"EUR_per_kWh":0.10985,"EXR":11.2515,"time_start":"2023-04-05T23:00:00+02:00","time_end":"2023-04-06T00:00:00+02:00"}]""",
            """[{"NOK_per_kWh":1.2895,"EUR_per_kWh":0.11478,"EXR":11.2345,"time_start":"2023-04-06T00:00:00+02:00","time_end":"2023-04-06T01:00:00+02:00"},{"NOK_per_kWh":1.2759,"EUR_per_kWh":0.11357,"EXR":11.2345,"time_start":"2023-04-06T01:00:00+02:00","time_end":"2023-04-06T02:00:00+02:00"},{"NOK_per_kWh":1.27512,"EUR_per_kWh":0.1135,"EXR":11.2345,"time_start":"2023-04-06T02:00:00+02:00","time_end":"2023-04-06T03:00:00+02:00"},{"NOK_per_kWh":1.25546,"EUR_per_kWh":0.11175,"EXR":11.2345,"time_start":"2023-04-06T03:00:00+02:00","time_end":"2023-04-06T04:00:00+02:00"},{"NOK_per_kWh":1.25029,"EUR_per_kWh":0.11129,"EXR":11.2345,"time_start":"2023-04-06T04:00:00+02:00","time_end":"2023-04-06T05:00:00+02:00"},{"NOK_per_kWh":1.29073,"EUR_per_kWh":0.11489,"EXR":11.2345,"time_start":"2023-04-06T05:00:00+02:00","time_end":"2023-04-06T06:00:00+02:00"},{"NOK_per_kWh":1.28983,"EUR_per_kWh":0.11481,"EXR":11.2345,"time_start":"2023-04-06T06:00:00+02:00","time_end":"2023-04-06T07:00:00+02:00"},{"NOK_per_kWh":1.32163,"EUR_per_kWh":0.11764,"EXR":11.2345,"time_start":"2023-04-06T07:00:00+02:00","time_end":"2023-04-06T08:00:00+02:00"},{"NOK_per_kWh":1.32623,"EUR_per_kWh":0.11805,"EXR":11.2345,"time_start":"2023-04-06T08:00:00+02:00","time_end":"2023-04-06T09:00:00+02:00"},{"NOK_per_kWh":1.31781,"EUR_per_kWh":0.1173,"EXR":11.2345,"time_start":"2023-04-06T09:00:00+02:00","time_end":"2023-04-06T10:00:00+02:00"},{"NOK_per_kWh":1.25692,"EUR_per_kWh":0.11188,"EXR":11.2345,"time_start":"2023-04-06T10:00:00+02:00","time_end":"2023-04-06T11:00:00+02:00"},{"NOK_per_kWh":1.20782,"EUR_per_kWh":0.10751,"EXR":11.2345,"time_start":"2023-04-06T11:00:00+02:00","time_end":"2023-04-06T12:00:00+02:00"},{"NOK_per_kWh":1.16861,"EUR_per_kWh":0.10402,"EXR":11.2345,"time_start":"2023-04-06T12:00:00+02:00","time_end":"2023-04-06T13:00:00+02:00"},{"NOK_per_kWh":1.15996,"EUR_per_kWh":0.10325,"EXR":11.2345,"time_start":"2023-04-06T13:00:00+02:00","time_end":"2023-04-06T14:00:00+02:00"},{"NOK_per_kWh":1.17086,"EUR_per_kWh":0.10422,"EXR":11.2345,"time_start":"2023-04-06T14:00:00+02:00","time_end":"2023-04-06T15:00:00+02:00"},{"NOK_per_kWh":1.17592,"EUR_per_kWh":0.10467,"EXR":11.2345,"time_start":"2023-04-06T15:00:00+02:00","time_end":"2023-04-06T16:00:00+02:00"},{"NOK_per_kWh":1.19614,"EUR_per_kWh":0.10647,"EXR":11.2345,"time_start":"2023-04-06T16:00:00+02:00","time_end":"2023-04-06T17:00:00+02:00"},{"NOK_per_kWh":1.25152,"EUR_per_kWh":0.1114,"EXR":11.2345,"time_start":"2023-04-06T17:00:00+02:00","time_end":"2023-04-06T18:00:00+02:00"},{"NOK_per_kWh":1.26635,"EUR_per_kWh":0.11272,"EXR":11.2345,"time_start":"2023-04-06T18:00:00+02:00","time_end":"2023-04-06T19:00:00+02:00"},{"NOK_per_kWh":1.2695,"EUR_per_kWh":0.113,"EXR":11.2345,"time_start":"2023-04-06T19:00:00+02:00","time_end":"2023-04-06T20:00:00+02:00"},{"NOK_per_kWh":1.26186,"EUR_per_kWh":0.11232,"EXR":11.2345,"time_start":"2023-04-06T20:00:00+02:00","time_end":"2023-04-06T21:00:00+02:00"},{"NOK_per_kWh":1.24051,"EUR_per_kWh":0.11042,"EXR":11.2345,"time_start":"2023-04-06T21:00:00+02:00","time_end":"2023-04-06T22:00:00+02:00"},{"NOK_per_kWh":1.22456,"EUR_per_kWh":0.109,"EXR":11.2345,"time_start":"2023-04-06T22:00:00+02:00","time_end":"2023-04-06T23:00:00+02:00"},{"NOK_per_kWh":1.2031,"EUR_per_kWh":0.10709,"EXR":11.2345,"time_start":"2023-04-06T23:00:00+02:00","time_end":"2023-04-07T00:00:00+02:00"}]""",
            """[{"NOK_per_kWh":1.12599,"EUR_per_kWh":0.09878,"EXR":11.399,"time_start":"2023-04-07T00:00:00+02:00","time_end":"2023-04-07T01:00:00+02:00"},{"NOK_per_kWh":1.11459,"EUR_per_kWh":0.09778,"EXR":11.399,"time_start":"2023-04-07T01:00:00+02:00","time_end":"2023-04-07T02:00:00+02:00"},{"NOK_per_kWh":1.1195,"EUR_per_kWh":0.09821,"EXR":11.399,"time_start":"2023-04-07T02:00:00+02:00","time_end":"2023-04-07T03:00:00+02:00"},{"NOK_per_kWh":1.12611,"EUR_per_kWh":0.09879,"EXR":11.399,"time_start":"2023-04-07T03:00:00+02:00","time_end":"2023-04-07T04:00:00+02:00"},{"NOK_per_kWh":1.13933,"EUR_per_kWh":0.09995,"EXR":11.399,"time_start":"2023-04-07T04:00:00+02:00","time_end":"2023-04-07T05:00:00+02:00"},{"NOK_per_kWh":1.18413,"EUR_per_kWh":0.10388,"EXR":11.399,"time_start":"2023-04-07T05:00:00+02:00","time_end":"2023-04-07T06:00:00+02:00"},{"NOK_per_kWh":1.17341,"EUR_per_kWh":0.10294,"EXR":11.399,"time_start":"2023-04-07T06:00:00+02:00","time_end":"2023-04-07T07:00:00+02:00"},{"NOK_per_kWh":1.19165,"EUR_per_kWh":0.10454,"EXR":11.399,"time_start":"2023-04-07T07:00:00+02:00","time_end":"2023-04-07T08:00:00+02:00"},{"NOK_per_kWh":1.22311,"EUR_per_kWh":0.1073,"EXR":11.399,"time_start":"2023-04-07T08:00:00+02:00","time_end":"2023-04-07T09:00:00+02:00"},{"NOK_per_kWh":1.18493,"EUR_per_kWh":0.10395,"EXR":11.399,"time_start":"2023-04-07T09:00:00+02:00","time_end":"2023-04-07T10:00:00+02:00"},{"NOK_per_kWh":1.15996,"EUR_per_kWh":0.10176,"EXR":11.399,"time_start":"2023-04-07T10:00:00+02:00","time_end":"2023-04-07T11:00:00+02:00"},{"NOK_per_kWh":1.15016,"EUR_per_kWh":0.1009,"EXR":11.399,"time_start":"2023-04-07T11:00:00+02:00","time_end":"2023-04-07T12:00:00+02:00"},{"NOK_per_kWh":1.08188,"EUR_per_kWh":0.09491,"EXR":11.399,"time_start":"2023-04-07T12:00:00+02:00","time_end":"2023-04-07T13:00:00+02:00"},{"NOK_per_kWh":1.02078,"EUR_per_kWh":0.08955,"EXR":11.399,"time_start":"2023-04-07T13:00:00+02:00","time_end":"2023-04-07T14:00:00+02:00"},{"NOK_per_kWh":1.01428,"EUR_per_kWh":0.08898,"EXR":11.399,"time_start":"2023-04-07T14:00:00+02:00","time_end":"2023-04-07T15:00:00+02:00"},{"NOK_per_kWh":1.06934,"EUR_per_kWh":0.09381,"EXR":11.399,"time_start":"2023-04-07T15:00:00+02:00","time_end":"2023-04-07T16:00:00+02:00"},{"NOK_per_kWh":1.15175,"EUR_per_kWh":0.10104,"EXR":11.399,"time_start":"2023-04-07T16:00:00+02:00","time_end":"2023-04-07T17:00:00+02:00"},{"NOK_per_kWh":1.22551,"EUR_per_kWh":0.10751,"EXR":11.399,"time_start":"2023-04-07T17:00:00+02:00","time_end":"2023-04-07T18:00:00+02:00"},{"NOK_per_kWh":1.26461,"EUR_per_kWh":0.11094,"EXR":11.399,"time_start":"2023-04-07T18:00:00+02:00","time_end":"2023-04-07T19:00:00+02:00"},{"NOK_per_kWh":1.26882,"EUR_per_kWh":0.11131,"EXR":11.399,"time_start":"2023-04-07T19:00:00+02:00","time_end":"2023-04-07T20:00:00+02:00"},{"NOK_per_kWh":1.2792,"EUR_per_kWh":0.11222,"EXR":11.399,"time_start":"2023-04-07T20:00:00+02:00","time_end":"2023-04-07T21:00:00+02:00"},{"NOK_per_kWh":1.2874,"EUR_per_kWh":0.11294,"EXR":11.399,"time_start":"2023-04-07T21:00:00+02:00","time_end":"2023-04-07T22:00:00+02:00"},{"NOK_per_kWh":1.28695,"EUR_per_kWh":0.1129,"EXR":11.399,"time_start":"2023-04-07T22:00:00+02:00","time_end":"2023-04-07T23:00:00+02:00"},{"NOK_per_kWh":1.27418,"EUR_per_kWh":0.11178,"EXR":11.399,"time_start":"2023-04-07T23:00:00+02:00","time_end":"2023-04-08T00:00:00+02:00"}]"""
        )

        val client = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json()
            }
            engine {
                addHandler { request ->
                    when (request.url.toString()) {
                        urls[0] -> {
                            respond(
                                responses[0], headers = headersOf(
                                    "Content-Type" to listOf("application/json")))
                        }
                        urls[1] -> {
                            respond(
                                responses[1], headers = headersOf(
                                    "Content-Type" to listOf("application/json")))
                        }
                        urls[2] -> {
                            respond(
                                responses[2], headers = headersOf(
                                    "Content-Type" to listOf("application/json")))
                        }
                        urls[3] -> {
                            respond(
                                responses[3], headers = headersOf(
                                    "Content-Type" to listOf("application/json")))
                        }
                        urls[4] -> {
                            respond(
                                responses[4], headers = headersOf(
                                    "Content-Type" to listOf("application/json")))
                        }
                        urls[5] -> {
                            respond(
                                responses[5], headers = headersOf(
                                    "Content-Type" to listOf("application/json")))
                        }
                        urls[6] -> {
                            respond(
                                responses[6], headers = headersOf(
                                    "Content-Type" to listOf("application/json")))
                        }
                        else -> error("Unhandled ${request.url}")
                    }
                }
            }
        }

        val electricityPrice = ElectricityPrice(client)
        val result = electricityPrice.getWeek(Region.NO5, mockedCalendar)

        assertEquals(7, result.days.size)
    }

    /**
     * Creates a mock HttpClient with the specified URL and response.
     *
     * This function sets up an HttpClient with a MockEngine to return the provided result when
     * the specified URL is requested.
     *
     * @param url The URL that should be intercepted and return the mock response.
     * @param result The mock response (as a JSON string) to return when the URL is requested.
     * @return A mock HttpClient configured with the specified URL and response.
     */
    private fun createMockClient(url: String, result: String): HttpClient {
        return HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json()
            }
            engine {
                addHandler { request ->
                    when (request.url.toString()) {
                        url -> {
                            respond(
                                result,
                                headers = headersOf(
                                    "Content-Type" to listOf("application/json")
                                )
                            )
                        }
                        else -> error("Unhandled ${request.url}")
                    }
                }
            }
        }
    }
}

 */