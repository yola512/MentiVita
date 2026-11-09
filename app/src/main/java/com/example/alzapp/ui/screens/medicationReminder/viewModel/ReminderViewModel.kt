package com.example.alzapp.ui.screens.medicationReminder.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alzapp.data.notifications.ActiveNotificationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReminderUiState(
    val activeReminders: List<ReminderData> = emptyList()
)

data class ReminderData(
    val id: Int,
    val name: String,
    val dose: String
)

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderManager: ActiveNotificationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReminderUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeReminders()
    }

    private fun observeReminders() {
        viewModelScope.launch {
            reminderManager.currentReminders.collect { reminders ->
                _uiState.update { state ->
                    state.copy(activeReminders = reminders.map { 
                        ReminderData(it.id, it.name, it.dose)
                    })
                }
            }
        }
    }
}
