package com.example.alzapp.data.notifications.local

import androidx.room.Entity

@Entity(
    tableName = "pending_snoozes",
    primaryKeys = ["medId", "medTimeStr"]
)
data class SnoozeEntity(
    val medId: Int,
    val medName: String,
    val medDose: String,
    val medTimeStr: String,
    val triggerTime: Long
)