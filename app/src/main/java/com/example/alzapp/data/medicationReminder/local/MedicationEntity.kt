package com.example.alzapp.data.medicationReminder.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dose: String, // amount + unit (pills/drops/ml)
    val startDate: LocalDate,
    val endDate: LocalDate,
    val takingTimes: List<LocalDateTime>,
    val takingDays: Set<String>,
    val takenDateTimes: Set<LocalDateTime> = emptySet(), // track specific time of taking
    val addedDate: LocalDateTime = LocalDateTime.now() // creation date
)