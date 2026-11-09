package com.example.alzapp.data.todo.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(tableName = "todos")
data class TodoTaskEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val taskDescription: String = "",
    var isCompleted: Boolean = false,
    val date: LocalDateTime = LocalDateTime.now(),
    val time: LocalTime? = null,
    val reminderMinutesBefore: Int? = null // e.g. 15, 30 or null if no reminder
)