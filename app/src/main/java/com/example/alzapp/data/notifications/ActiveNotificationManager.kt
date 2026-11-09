package com.example.alzapp.data.notifications

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActiveNotificationManager @Inject constructor() {
    data class ReminderData(
        val id: Int,
        val name: String,
        val dose: String,
        val time: LocalTime? = null,
        val timestamp: Long = System.currentTimeMillis(),
        val isTodo: Boolean = false,
        val reminderMins: Int? = null
    )

    private val _currentReminders = MutableStateFlow<List<ReminderData>>(emptyList())
    val currentReminders = _currentReminders.asStateFlow()

    // show medication reminder
    fun showReminder(id: Int, name: String, dose: String, time: LocalTime? = null) {
        val currentList = _currentReminders.value
        // only add if not already in the list
        if (currentList.none { it.id == id && it.time == time && !it.isTodo }) {
            _currentReminders.value = currentList + ReminderData(
                id = id,
                name = name,
                dose = dose,
                time = time,
                isTodo = false
            )
        }
    }

    // show task reminder
    fun showTodoReminder(id: Int, title: String, desc: String, reminderMins: Int) {
        val currentList = _currentReminders.value
        // only add if not already in the list
        if (currentList.none { it.id == id && it.isTodo }) {
            _currentReminders.value = currentList + ReminderData(
                id = id,
                name = title,
                dose = desc,
                isTodo = true,
                reminderMins = reminderMins
            )
        }
    }

    // dismiss everything
    fun dismissAll() {
        _currentReminders.value = emptyList()
    }
}
