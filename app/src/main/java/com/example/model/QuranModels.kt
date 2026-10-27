package com.example.model

data class Surah(
    val id: Int,
    val nameAr: String,
    val nameEn: String,
    val versesCount: Int,
    val revelationType: String, // مكية / مدنية
    val pageNumber: Int,
    val juzNumber: Int
)

data class Ayah(
    val surahId: Int,
    val numberInSurah: Int,
    val numberInQuran: Int,
    val textAr: String,
    val textEn: String = "",
    val tafseerSaadi: String = "",
    val tafseerJalalayn: String = "",
    val tafseerMuyassar: String = "",
    val audioUrl: String = ""
)

data class Reciter(
    val id: String,
    val nameAr: String,
    val styleAr: String,
    val serverBaseUrl: String
)

data class Bookmark(
    val id: String,
    val surahId: Int,
    val surahName: String,
    val ayahNumber: Int,
    val ayahTextSnippet: String,
    val timestamp: Long
)
