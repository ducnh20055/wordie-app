package com.example.wordie.models

import com.google.firebase.firestore.PropertyName

data class AppUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val level: String = "Beginner",
    @get:PropertyName("isPremium")
    val isPremium: Boolean = false,
    val streak: Int = 0,
    val lastActiveDate: String = "",
    val xp: Int = 0,
    val lessonsDone: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
