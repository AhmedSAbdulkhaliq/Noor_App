package com.example.model

enum class PrayerName(val arabicName: String) {
    FAJR("الفجر"),
    SUNRISE("الشروق"),
    DHUHR("الظهر"),
    ASR("العصر"),
    MAGHRIB("المغرب"),
    ISHA("العشاء")
}

data class PrayerTimeItem(
    val prayer: PrayerName,
    val timeFormatted: String,
    val timeMinutesOfDay: Int,
    val isNext: Boolean = false,
    val isPast: Boolean = false
)

enum class CalculationMethod(
    val titleAr: String,
    val fajrAngle: Double,
    val ishaAngle: Double,
    val ishaMinutesAfterMaghrib: Int = 0
) {
    EGYPTIAN("الهيئة المصرية العامة للمساحة", 19.5, 17.5),
    MWL("رابطة العالم الإسلامي", 18.0, 17.0),
    MAKKAH("جامعة أم القرى (مكة المكرمة)", 18.5, 0.0, ishaMinutesAfterMaghrib = 90),
    ISNA("الجمعية الإسلامية لأمريكا الشمالية (ISNA)", 15.0, 15.0),
    KARACHI("جامعة العلوم الإسلامية بكراتشي", 18.0, 18.0)
}

data class LocationCity(
    val nameAr: String,
    val countryAr: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double
)

data class QadaRecord(
    val fajr: Int = 0,
    val dhuhr: Int = 0,
    val asr: Int = 0,
    val maghrib: Int = 0,
    val isha: Int = 0
) {
    val total: Int get() = fajr + dhuhr + asr + maghrib + isha
}
