package com.example.stromkalkulator.domain

import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.models.electricity.HourPrice
import com.example.stromkalkulator.data.repositories.ElectricityPrice
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar

internal class ElectricityPriceDomainTest {
    private lateinit var mockedCalendar: Calendar
    private lateinit var hourPrice: HourPrice

    @Before
    fun setUp() {
        // Mocks the calendar to return a specific date and time

        mockkClass(Calendar::class, relaxed = true)
        mockedCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 3)
            set(Calendar.DAY_OF_MONTH, 7)
            set(Calendar.DAY_OF_YEAR, 97)
            set(Calendar.HOUR_OF_DAY, 15)
        }

        mockkObject(ElectricityPrice)

        hourPrice = HourPrice(
            NOK_per_kWh = 0.96432,
            EUR_per_kWh = 0.08491,
            EXR = 11.357,
            time_start = "2023-04-01T00:00:00+02:00",
            time_end = "2023-04-01T01:00:00+02:00"
        )

    }

    @Test
    fun testCurrentHour() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns List(24) { hourPrice }
        val result = runBlocking {  ElectricityPriceDomain.getCurrentHour(Region.NO1, mockedCalendar) }

        assertEquals(0.96432, result, 0.0)
    }

    @Test
    fun testCurrentHourEmpty() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns emptyList()
        val result = runBlocking {  ElectricityPriceDomain.getCurrentHour(Region.NO1) }

        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun testCurrentHourExeption() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } throws Exception()
        val result = runBlocking {  ElectricityPriceDomain.getCurrentHour(Region.NO1) }

        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun testGetToday() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns List(24) { hourPrice }
        val result = runBlocking {  ElectricityPriceDomain.getToday(Region.NO1, mockedCalendar) }

        assertEquals(24, result.size)
        assertEquals(0.96432, result[0], 0.0)
    }

    @Test
    fun testGetTodayEmpty() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns emptyList()
        val result = runBlocking {  ElectricityPriceDomain.getToday(Region.NO1) }

        assertEquals(0, result.size)
    }

    @Test
    fun testGetTodayExeption() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } throws Exception()
        val result = runBlocking {  ElectricityPriceDomain.getToday(Region.NO1) }

        assertEquals(0, result.size)
    }

    @Test
    fun testWeekAverages() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns List(24) { hourPrice }
        val result = runBlocking {  ElectricityPriceDomain.getWeekAverages(Region.NO1, mockedCalendar) }

        assertEquals(7, result.size)
        assertEquals(0.96432, result[0], 0.000001)
    }

    @Test
    fun testWeekAveragesEmpty() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns emptyList()
        val result = runBlocking {  ElectricityPriceDomain.getWeekAverages(Region.NO1) }

        assertEquals(7, result.size)
    }

    @Test
    fun testWeekAveragesExeption() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } throws Exception()
        val result = runBlocking {  ElectricityPriceDomain.getWeekAverages(Region.NO1) }

        assertEquals(7, result.size)
    }

    @Test
    fun testGetMonthAverages() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns List(24) { hourPrice }
        val result = runBlocking {  ElectricityPriceDomain.getMonthAverages(Region.NO1, mockedCalendar) }

        assertEquals(30, result.size)
        assertEquals(0.96432, result[0], 0.000001)
    }

    @Test
    fun testGetMonthAveragesEmpty() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns emptyList()
        val result = runBlocking {  ElectricityPriceDomain.getMonthAverages(Region.NO1) }

        assertEquals(30, result.size)
    }

    @Test
    fun testGetMonthAveragesExeption() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } throws Exception()
        val result = runBlocking {  ElectricityPriceDomain.getMonthAverages(Region.NO1) }

        assertEquals(30, result.size)
    }

//    @Test
//    fun testGetTomorrow() {
//        ElectricityPriceDomain.reset()
//        coEvery { ElectricityPrice.getDay(any(), any()) } returns List(24) { hourPrice }
//        val result = runBlocking {  ElectricityPriceDomain.getTomorrow(Region.NO1, mockedCalendar) }
//
//        assertEquals(24, result.size)
//        assertEquals(0.96432, result[0], 0.0)
//    }
//
//    @Test
//    fun testGetTomorrowEmpty() {
//        ElectricityPriceDomain.reset()
//        coEvery { ElectricityPrice.getDay(any(), any()) } returns emptyList()
//        val result = runBlocking {  ElectricityPriceDomain.getTomorrow(Region.NO1) }
//
//        assertEquals(0, result.size)
//    }
//
//    @Test
//    fun testGetTomorowExeption() {
//        ElectricityPriceDomain.reset()
//        coEvery { ElectricityPrice.getDay(any(), any()) } throws Exception()
//        val result = runBlocking {  ElectricityPriceDomain.getTomorrow(Region.NO1) }
//
//        assertEquals(0, result.size)
//    }

    @Test
    fun testGetWeek() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns List(24) { hourPrice }
        val result = runBlocking {  ElectricityPriceDomain.getWeek(Region.NO1, mockedCalendar) }

        assertEquals(7, result.size)
        assertEquals(24, result[0].size)
        assertEquals(0.96432, result[0][0], 0.0)
    }

    @Test
    fun testGetWeekEmpty() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns emptyList()
        val result = runBlocking {  ElectricityPriceDomain.getWeek(Region.NO1) }

        assertEquals(7, result.size)
        assertEquals(0, result[0].size)
    }

    @Test
    fun testWeekException() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } throws Exception()
        val result = runBlocking {  ElectricityPriceDomain.getWeek(Region.NO1) }

        assertEquals(7, result.size)
        assertEquals(0, result[0].size)
    }

    @Test
    fun testGetMonth() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns List(24) { hourPrice }
        val result = runBlocking {  ElectricityPriceDomain.getMonth(Region.NO1, mockedCalendar) }

        assertEquals(30, result.size)
        assertEquals(24, result[0].size)
        assertEquals(0.96432, result[0][0], 0.0)
    }

    @Test
    fun testGetMonthEmpty() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns emptyList()
        val result = runBlocking {  ElectricityPriceDomain.getMonth(Region.NO1) }

        assertEquals(30, result.size)
        assertEquals(0, result[0].size)
    }

    @Test
    fun testGetMonthException() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } throws Exception()
        val result = runBlocking {  ElectricityPriceDomain.getMonth(Region.NO1) }

        assertEquals(30, result.size)
        assertEquals(0, result[0].size)
    }

    @Test
    fun testFetchIfNeeded() {
        ElectricityPriceDomain.reset()
        coEvery { ElectricityPrice.getDay(any(), any()) } returns List(24) { hourPrice }
        val result = runBlocking {  ElectricityPriceDomain.getWeek(Region.NO1, mockedCalendar) }
        assertEquals(7, result.size)
        assertEquals(24, result[0].size)
        assertEquals(0.96432, result[0][0], 0.0)

        coEvery { ElectricityPrice.getDay(any(), any()) } returns List(24) { hourPrice }
        val result2 = runBlocking {  ElectricityPriceDomain.getWeek(Region.NO1, mockedCalendar) }
        assertEquals(7, result2.size)
        assertEquals(24, result2[0].size)
        assertEquals(0.96432, result2[0][0], 0.0)

        coEvery { ElectricityPrice.getDay(any(), any()) } returns emptyList()
        val result3 = runBlocking {  ElectricityPriceDomain.getWeek(Region.NO1, mockedCalendar) }
        assertEquals(7, result3.size)
        assertEquals(0, result3[0].size)
    }
}