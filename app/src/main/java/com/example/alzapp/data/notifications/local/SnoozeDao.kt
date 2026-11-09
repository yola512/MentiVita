package com.example.alzapp.data.notifications.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SnoozeDao {
    @Query("SELECT * FROM pending_snoozes")
    suspend fun getAllSnoozes(): List<SnoozeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnooze(snooze: SnoozeEntity)

    @Query("DELETE FROM pending_snoozes WHERE medId = :medId AND medTimeStr = :timeStr")
    suspend fun deleteSnooze(medId: Int, timeStr: String)
}