package com.example.data

import com.example.model.CalculationMethod
import com.example.model.PrayerName
import com.example.model.PrayerTimeItem
import java.util.Calendar
import kotlin.math.*

object PrayerTimeCalculator {

    // Default reference cities across the Islamic world
    val PRESET_CITIES = listOf(
        com.example.model.LocationCity("مكة المكرمة", "المملكة العربية السعودية", 21.4225, 39.8262, 3.0),
        com.example.model.LocationCity("المدينة المنورة", "المملكة العربية السعودية", 24.4672, 39.6111, 3.0),
        com.example.model.LocationCity("القدس الشريف", "فلسطين", 31.7683, 35.2137, 3.0),
        com.example.model.LocationCity("القاهرة", "مصر", 30.0444, 31.2357, 2.0),
        com.example.model.LocationCity("الرياض", "المملكة العربية السعودية", 24.7136, 46.6753, 3.0),
        com.example.model.LocationCity("دبي", "الإمارات العربية المتحدة", 25.2048, 55.2708, 4.0),
        com.example.model.LocationCity("عمان", "الأردن", 31.9539, 35.9106, 3.0),
        com.example.model.LocationCity("بغداد", "العراق", 33.3152, 44.3661, 3.0),
        com.example.model.LocationCity("دمشق", "سوريا", 33.5138, 36.2765, 3.0),
        com.example.model.LocationCity("بيروت", "لبنان", 33.8938, 35.5018, 3.0),
        com.example.model.LocationCity("إسطنبول", "تركيا", 41.0082, 28.9784, 3.0),
        com.example.model.LocationCity("الرباط", "المغرب", 34.0209, -6.8416, 1.0),
        com.example.model.LocationCity("تونس", "تونس", 36.8065, 10.1815, 1.0),
        com.example.model.LocationCity("الجزائر", "الجزائر", 36.7538, 3.0588, 1.0),
        com.example.model.LocationCity("الكويت", "الكويت", 29.3759, 47.9774, 3.0),
        com.example.model.LocationCity("الدوحة", "قطر", 25.2854, 51.5310, 3.0),
        com.example.model.LocationCity("مسقط", "عُمان", 23.5880, 58.3829, 4.0),
        com.example.model.LocationCity("المنامة", "البحرين", 26.2285, 50.5860, 3.0),
        com.example.model.LocationCity("الخرطوم", "السودان", 15.5007, 32.5599, 2.0),
        com.example.model.LocationCity("كوالالمبور", "ماليزيا", 3.1390, 101.6869, 8.0),
        com.example.model.LocationCity("جاكرتا", "إندونيسيا", -6.2088, 106.8456, 7.0),
        com.example.model.LocationCity("لندن", "المملكة المتحدة", 51.5074, -0.1278, 0.0),
        com.example.model.LocationCity("باريس", "فرنسا", 48.8566, 2.3522, 1.0)
    )

    fun calculateTimes(
        calendar: Calendar = Calendar.getInstance(),
        lat: Double,
        lng: Double,
        timezoneHours: Double,
        method: CalculationMethod = CalculationMethod.EGYPTIAN
    ): List<PrayerTimeItem> {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Julian Day calculation
        val julianDate = getJulianDate(year, month, day)
        val d = julianDate - 2451545.0

        // Sun position
        val q = 280.459 + 0.98564736 * d
        val g = Math.toRadians((357.529 + 0.98560028 * d) % 360)
        val l = (q + 1.915 * sin(g) + 0.020 * sin(2 * g)) % 360
        val e = 23.439 - 0.00000036 * d
        val lRad = Math.toRadians(l)
        val eRad = Math.toRadians(e)

        val ra = (Math.toDegrees(atan2(cos(eRad) * sin(lRad), cos(lRad))) + 360) % 360
        val declination = Math.toDegrees(asin(sin(eRad) * sin(lRad)))
        val eqOfTime = (q / 15.0) - (ra / 15.0) // in hours

        // Solar noon (Dhuhr)
        val noon = (12.0 + timezoneHours - (lng / 15.0) - eqOfTime)

        // Helper for hour angles
        fun getHourAngle(angleDeg: Double): Double {
            val phi = Math.toRadians(lat)
            val delta = Math.toRadians(declination)
            val alpha = Math.toRadians(angleDeg)
            val cosHA = (sin(alpha) - sin(phi) * sin(delta)) / (cos(phi) * cos(delta))
            val clamped = cosHA.coerceIn(-1.0, 1.0)
            return Math.toDegrees(acos(clamped)) / 15.0
        }

        // Sunrise & Sunset angle is approximately -0.833 degrees (accounting for refraction and solar radius)
        val sunRiseHourAngle = getHourAngle(-0.833)
        val sunrise = noon - sunRiseHourAngle
        val sunset = noon + sunRiseHourAngle

        // Fajr
        val fajrHourAngle = getHourAngle(-method.fajrAngle)
        val fajr = noon - fajrHourAngle

        // Asr (Standard: shadow length = object + noon shadow)
        val phi = Math.toRadians(lat)
        val delta = Math.toRadians(declination)
        val noonAngle = abs(phi - delta)
        val asrAltitude = Math.toDegrees(atan(1.0 / (1.0 + tan(noonAngle))))
        val asrHourAngle = getHourAngle(asrAltitude)
        val asr = noon + asrHourAngle

        // Maghrib is equal to sunset + 1-2 minutes buffer
        val maghrib = sunset + (2.0 / 60.0)

        // Isha
        val isha = if (method.ishaMinutesAfterMaghrib > 0) {
            maghrib + (method.ishaMinutesAfterMaghrib.toDouble() / 60.0)
        } else {
            val ishaHourAngle = getHourAngle(-method.ishaAngle)
            noon + ishaHourAngle
        }

        // Format times
        val currentMinutesOfDay = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)

        val rawTimes = listOf(
            PrayerName.FAJR to fajr,
            PrayerName.SUNRISE to sunrise,
            PrayerName.DHUHR to noon,
            PrayerName.ASR to asr,
            PrayerName.MAGHRIB to maghrib,
            PrayerName.ISHA to isha
        )

        var nextFound = false
        val items = mutableListOf<PrayerTimeItem>()

        for ((name, hourVal) in rawTimes) {
            val normalizedHour = (hourVal + 24.0) % 24.0
            val totalMinutes = (normalizedHour * 60).roundToInt()
            val hours = totalMinutes / 60
            val mins = totalMinutes % 60
            val formatted = String.format("%02d:%02d", hours, mins)

            val isPast = totalMinutes < currentMinutesOfDay
            val isNext = !isPast && !nextFound && name != PrayerName.SUNRISE
            if (isNext) {
                nextFound = true
            }

            items.add(
                PrayerTimeItem(
                    prayer = name,
                    timeFormatted = formatted,
                    timeMinutesOfDay = totalMinutes,
                    isNext = isNext,
                    isPast = isPast
                )
            )
        }

        // If all prayers today are past, Fajr tomorrow is next
        if (!nextFound && items.isNotEmpty()) {
            val fajrItem = items.first()
            items[0] = fajrItem.copy(isNext = true)
        }

        return items
    }

    private fun getJulianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }
}
