package com.example.alzapp.data.games.cryptogram.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProverbDao {
    @Query("SELECT * FROM proverbs WHERE isCompleted = 0 AND userGuesses IS NOT NULL LIMIT 1")
    suspend fun getUnfinishedProverb(): ProverbEntity?

    @Query("SELECT * FROM proverbs WHERE isCompleted = 0 AND userGuesses IS NULL ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomNewProverb(): ProverbEntity?

    @Query("SELECT COUNT(*) FROM proverbs")
    suspend fun getProverbCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProverbs(proverbs: List<ProverbEntity>)

    @Update
    suspend fun updateProverb(proverb: ProverbEntity)
}