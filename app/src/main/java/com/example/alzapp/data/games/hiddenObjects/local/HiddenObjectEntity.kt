package com.example.alzapp.data.games.hiddenObjects.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alzapp.data.games.GameCategory
import com.example.alzapp.data.games.hiddenObjects.model.HiddenObject

@Entity(tableName = "hidden_object_games")
data class HiddenObjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val imageResId: Int,
    val objectsToFind: List<HiddenObject>,
    val foundObjectIds: Set<Int>,
    val isCompleted: Boolean,
    val category: GameCategory = GameCategory.ATTENTION
)