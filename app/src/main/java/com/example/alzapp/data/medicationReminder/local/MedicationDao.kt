package com.example.alzapp.data.medicationReminder.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: Int): MedicationEntity?

    @Query("SELECT * FROM medications WHERE takingTimes LIKE '%' || :date || '%'")
    fun getMedicationsByDate(date: String): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE takingTimes LIKE '%' || :date || '%'")
    suspend fun getMedicationsByDateSync(date: String): List<MedicationEntity>

    @Query("SELECT * FROM medications")
    suspend fun getAllMedicationsSync(): List<MedicationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity): Long

    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)
}