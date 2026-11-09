package com.example.alzapp.data.settings

import com.example.alzapp.data.settings.local.SettingsDao
import com.example.alzapp.data.settings.local.SettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val settingsDao: SettingsDao
) {
    fun getSettings(): Flow<SettingsEntity> {
        return settingsDao.getSettings().map { it ?: SettingsEntity() }
    }

    suspend fun updateSettings(settings: SettingsEntity) {
        settingsDao.insertSettings(settings)
    }
}
