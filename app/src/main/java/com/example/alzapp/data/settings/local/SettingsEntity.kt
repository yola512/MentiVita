package com.example.alzapp.data.settings.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alzapp.data.settings.FontSize

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val fontSize: FontSize = FontSize.MEDIUM // make font appear bigger/smaller (small/medium/big)
)