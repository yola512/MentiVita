package com.example.alzapp.data.medicationReminder

import com.example.alzapp.data.medicationReminder.local.MedicationDao
import com.example.alzapp.data.medicationReminder.local.MedicationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationRepository @Inject constructor(
    private val medDao: MedicationDao
){
    val allMedications: Flow<List<MedicationEntity>> = medDao.getAllMedications()

    suspend fun getMedicationById(id: Int): MedicationEntity? {
        return medDao.getMedicationById(id)
    }

    suspend fun getAllMedicationsSync(): List<MedicationEntity> {
        return medDao.getAllMedicationsSync()
    }

    suspend fun addMedication(medication: MedicationEntity): Long {
        return medDao.insertMedication(medication)
    }
    suspend fun deleteMedication(medication: MedicationEntity) {
        medDao.deleteMedication(medication)
    }
    // updates medication bc of onConflict = OnConflictStrategy.REPLACE
    suspend fun updateMedication(medication: MedicationEntity) {
        medDao.insertMedication(medication)
    }
}