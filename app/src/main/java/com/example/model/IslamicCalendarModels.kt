package com.example.model

data class HijriDate(
    val day: Int,
    val monthNumber: Int,
    val monthNameAr: String,
    val year: Int,
    val formatted: String
)

data class ReligiousOccasion(
    val titleAr: String,
    val hijriDateDesc: String,
    val daysRemaining: Int,
    val descriptionAr: String
)

data class DailyVerse(
    val ayahText: String,
    val surahName: String,
    val ayahNumber: Int,
    val tafseerSnippet: String
)

data class DailyHadith(
    val text: String,
    val narrator: String,
    val source: String,
    val explanation: String
)
