package com.example.alzapp.data.games.hiddenObjects.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HiddenObjectDao {
    @Query("SELECT * FROM hidden_object_games WHERE isCompleted = 0 LIMIT 1")
    suspend fun getUnfinishedGame(): HiddenObjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: HiddenObjectEntity): Long
}
