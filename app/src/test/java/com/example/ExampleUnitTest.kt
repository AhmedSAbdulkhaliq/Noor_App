package com.example

import com.example.data.AzkarRepository
import com.example.data.DailyContentProvider
import com.example.data.PrayerTimeCalculator
import com.example.data.QiblaCalculator
import com.example.data.QuranRepository
import com.example.model.CalculationMethod
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class ExampleUnitTest {

    @Test
    fun testQuranSurahsCount() {
        assertEquals("The Holy Quran must have 114 Surahs", 114, QuranRepository.SURAHS.size)
        assertEquals("First Surah should be Al-Fatiha", "الفاتحة", QuranRepository.SURAHS.first().nameAr)
        assertEquals("Last Surah should be An-Nas", "الناس", QuranRepository.SURAHS.last().nameAr)
    }

    @Test
    fun testPrayerTimesCalculation() {
        val cal = Calendar.getInstance()
        val cairo = PrayerTimeCalculator.PRESET_CITIES.first { it.nameAr == "القاهرة" }
        val times = PrayerTimeCalculator.calculateTimes(
            calendar = cal,
            lat = cairo.latitude,
            lng = cairo.longitude,
            timezoneHours = cairo.timezoneOffsetHours,
            method = CalculationMethod.EGYPTIAN
        )
        assertEquals("There should be 6 time points (Fajr, Sunrise, Dhuhr, Asr, Maghrib, Isha)", 6, times.size)
    }

    @Test
    fun testQiblaCalculator() {
        val bearing = QiblaCalculator.calculateBearing(30.0444, 31.2357)
        assertTrue("Qibla bearing from Cairo should be ~136 degrees", bearing in 130f..142f)

        val distance = QiblaCalculator.calculateDistanceKm(30.0444, 31.2357)
        assertTrue("Distance from Cairo to Makkah is ~1280 km", distance in 1200..1400)
    }

    @Test
    fun testAzkarAndDailyInspiration() {
        assertTrue("Azkar categories should not be empty", AzkarRepository.CATEGORIES.isNotEmpty())
        val morningAzkar = AzkarRepository.getAzkarByCategory("morning")
        assertTrue("Morning Azkar should contain items", morningAzkar.isNotEmpty())

        val dailyVerse = DailyContentProvider.getTodayVerse()
        assertNotNull("Daily verse should be present", dailyVerse.ayahText)

        val hijriDate = DailyContentProvider.getHijriDate()
        assertNotNull("Hijri date should be formatted", hijriDate.formatted)
    }
}
