package com.example.alzapp.data.user

import com.example.alzapp.data.user.local.AchievementDao
import com.example.alzapp.data.user.local.AchievementEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepository @Inject constructor(
    private val achievementDao: AchievementDao
) {
    fun getAllAchievements(): Flow<List<AchievementEntity>> {
        return achievementDao.getAllAchievements()
    }

    suspend fun unlockAchievement(id: String) {
        val achievement = achievementDao.getAchievementById(id)
        if (achievement != null && !achievement.isUnlocked) {
            achievementDao.updateAchievement(achievement.copy(isUnlocked = true, progress = 100f))
        }
    }
}
