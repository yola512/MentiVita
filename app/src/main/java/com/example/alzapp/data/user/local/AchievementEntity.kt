package com.example.alzapp.data.user.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alzapp.data.user.AchievementCategory

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconRes: Int,
    val isUnlocked: Boolean = false,
    val progress: Float = 0f, // locked achievement is 0f, unlocked - 100f
    val category: AchievementCategory // e.g. "STREAK", "SUDOKU", "POSTMAN", "PROVERBS"
)