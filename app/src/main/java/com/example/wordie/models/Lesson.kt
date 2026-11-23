// models/Lesson.kt
package com.example.wordie.models

data class Lesson(
    val id: String = "",
    val title: String = "",
    val icon: String = "",
    val info: String = "",
    val level: String = "Easy",
    val order: Int = 0,
    val isPremium: Boolean = false,
    val words: List<WordItem> = emptyList()
)

data class WordItem(
    val id: String = "",
    val emoji: String = "",
    val english: String = "",
    val vietnamese: String = "",
    val example: String = ""
)