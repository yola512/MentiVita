package com.example.alzapp.data.user.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 1,
    val currentStreak: Int = 0,
    val lastActivityTimestamp: Long = 0L,
    val activityDates: Set<LocalDate> = emptySet()
)