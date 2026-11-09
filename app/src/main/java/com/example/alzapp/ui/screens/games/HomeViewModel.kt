package com.example.alzapp.ui.screens.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alzapp.data.user.AchievementRepository
import com.example.alzapp.data.user.local.AchievementEntity
import com.example.alzapp.data.user.local.UserStatsEntity
import com.example.alzapp.data.user.UserStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userStatsRepository: UserStatsRepository,
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    val userStats: StateFlow<UserStatsEntity> = userStatsRepository.getUserStats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserStatsEntity()
        )

    val achievements: StateFlow<List<AchievementEntity>> = achievementRepository.getAllAchievements()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _hasSeenGrowthAnimation = MutableStateFlow(false)
    val hasSeenGrowthAnimation: StateFlow<Boolean> = _hasSeenGrowthAnimation.asStateFlow()

    fun markGrowthAnimationAsSeen() {
        _hasSeenGrowthAnimation.update { true }
    }
}
