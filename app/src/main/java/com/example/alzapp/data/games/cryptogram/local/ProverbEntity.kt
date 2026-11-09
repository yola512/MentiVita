package com.example.alzapp.data.games.cryptogram.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proverbs")
data class ProverbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val category: String,
    val isCompleted: Boolean = false,
    val cipherMap: String? = null,
    val userGuesses: String? = null
)