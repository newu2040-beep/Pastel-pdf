package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val actionType: String, // e.g., "Converted", "Signed", "Viewed"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1, // Only one profile for now
    val username: String = "User",
    val primaryColorHex: String = "#FFB3BA", // Pastel Pink default
    val notificationsEnabled: Boolean = true,
    val isDarkMode: Boolean = false
)
