package com.example.alzapp.ui.screens.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alzapp.data.settings.FontSize
import com.example.alzapp.data.settings.SettingsRepository
import com.example.alzapp.data.settings.local.SettingsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<SettingsEntity> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsEntity()
        )

    fun updateFontSize(fontSize: FontSize) {
        viewModelScope.launch {
            val currentSettings = settings.value
            settingsRepository.updateSettings(currentSettings.copy(fontSize = fontSize))
        }
    }
}