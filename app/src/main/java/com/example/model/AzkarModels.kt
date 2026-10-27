package com.example.model

data class ZikrCategory(
    val id: String,
    val titleAr: String,
    val descriptionAr: String,
    val iconName: String,
    val itemCount: Int
)

data class ZikrItem(
    val id: String,
    val categoryId: String,
    val textAr: String,
    val virtueAr: String,
    val sourceAr: String,
    val targetCount: Int,
    var currentCount: Int = 0
)
