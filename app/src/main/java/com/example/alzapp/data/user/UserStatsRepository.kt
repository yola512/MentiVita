package com.example.alzapp.data.user

import com.example.alzapp.data.user.local.UserStatsDao
import com.example.alzapp.data.user.local.UserStatsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStatsRepository @Inject constructor(
    private val userStatsDao: UserStatsDao,
    private val achievementRepository: AchievementRepository
) {
    fun getUserStats(): Flow<UserStatsEntity> = userStatsDao.getUserStats()
        .map { it ?: UserStatsEntity() }
        .map { stats ->
            val today = LocalDate.now()
            val lastDate = if (stats.lastActivityTimestamp == 0L) null
                           else LocalDate.ofEpochDay(stats.lastActivityTimestamp / (24 * 60 * 60 * 1000))

            // if the last activity was before yesterday, the streak is broken and should display as 0
            if (lastDate != null && lastDate.isBefore(today.minusDays(1))) {
                stats.copy(currentStreak = 0)
            } else {
                stats
            }
        }

    suspend fun updateStreak() {
        val stats = userStatsDao.getUserStatsSync() ?: UserStatsEntity()
        val today = LocalDate.now()
        // so-called Unix time
        // dividing lastActivityTimestamp by number of ms in one day -> we get number of days that have passed since 01.01.1970
        // those are later transformed into LocalDate format using ofEpochDay method
        val lastDate = if (stats.lastActivityTimestamp == 0L) null
                       else LocalDate.ofEpochDay(stats.lastActivityTimestamp / (24 * 60 * 60 * 1000)) // 24*60*60*1000 represents milliseconds in one day

        if (lastDate == today) return // don't increase streaks count if user has already increased their streak by playing one of available games

        // if last activity was yesterday, increment streak count; otherwise -
        val newStreak = if (lastDate == today.minusDays(1)) {
            stats.currentStreak + 1
        } else {
            1 // 1st day of the streak due to a break in playing games
        }

        val newActivityDates = stats.activityDates + today

        userStatsDao.saveUserStats(stats.copy(
            currentStreak = newStreak,
            lastActivityTimestamp = today.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            activityDates = newActivityDates
        ))

        // unlock "Pierwszy Kiełek" achievement
        achievementRepository.unlockAchievement("streak_1")
    }
}