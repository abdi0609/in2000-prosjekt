package com.example.stromkalkulator.domain

import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.data.repositories.TemperatureRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar


internal class TemperatureDomainTest {
private lateinit var mockedCalendar: Calendar

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

        mockkObject(TemperatureRepository)
    }


    @Test
    fun testGetTomorrow() {
        TemperatureDomain.reset()

        coEvery { TemperatureRepository.getTomorrow(any(),any(),any()) } returns List(24){13.5}
        val result = runBlocking { TemperatureDomain.getTomorrow(Region.NO1, mockedCalendar) }

        assertEquals(24, result.size)
    }

    @Test
    fun testGetWeek() {
        TemperatureDomain.reset()

        coEvery { TemperatureRepository.getPast(any(),any(),any(),any(),any()) } returns List(30){List(24){13.5}}
        val result = runBlocking { TemperatureDomain.getWeek(Region.NO1, mockedCalendar) }

        assertEquals(7, result.size)
    }

    @Test
    fun testGetMonth() {
        TemperatureDomain.reset()

        coEvery { TemperatureRepository.getPast(any(),any(),any(),any(),any()) } returns List(30){List(24){13.5}}
        val result = runBlocking { TemperatureDomain.getMonth(Region.NO1, mockedCalendar) }

        assertEquals(30, result.size)
    }
}