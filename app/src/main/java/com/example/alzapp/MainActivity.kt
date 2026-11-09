package com.example.alzapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.alzapp.data.notifications.ActiveNotificationManager
import com.example.alzapp.data.medicationReminder.MedicationScheduler
import com.example.alzapp.ui.navigation.MainRoot
import com.example.alzapp.ui.theme.AlzAppTheme
import com.example.alzapp.ui.components.ReminderPopUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var reminderManager: ActiveNotificationManager

    @Inject
    lateinit var scheduler: MedicationScheduler

    data class NavEvent(
        val medicationHighlights: List<Pair<Int, String?>> = emptyList(),
        val todoHighlightId: Int? = null,
        val navigateToTodo: Boolean = false
    )

    private val navigationEvents = MutableSharedFlow<NavEvent>(replay = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // handle intent if app was opened from notification
        handleIntent(intent)

        setContent {
            val currentReminders by reminderManager.currentReminders.collectAsState()
            var navEvent by remember { mutableStateOf(NavEvent()) }

            LaunchedEffect(Unit) {
                navigationEvents.collect { event ->
                    navEvent = event
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted -> }
                LaunchedEffect(Unit) {
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            AlzAppTheme {
                MainRoot(
                    modifier = Modifier,
                    highlightedReminders = navEvent.medicationHighlights,
                    highlightedTodoId = navEvent.todoHighlightId,
                    navigateToTodo = navEvent.navigateToTodo,
                    onTodoNavigated = { 
                        lifecycleScope.launch {
                            navigationEvents.emit(navEvent.copy(todoHighlightId = null, navigateToTodo = false))
                        }
                    }
                )

                if (currentReminders.isNotEmpty()) {
                    val isTodo = currentReminders.any { it.isTodo }
                    ReminderPopUp(
                        title = if (isTodo) "Nadchodzące zadanie" else "Czas wziąć leki!",
                        reminders = currentReminders,
                        onDismissRequest = { 
                            lifecycleScope.launch {
                                val medHighlights = currentReminders.filter { !it.isTodo }.map { it.id to it.time?.toString() }
                                val todoHighlight = currentReminders.find { it.isTodo }?.id
                                
                                navigationEvents.emit(NavEvent(
                                    medicationHighlights = medHighlights,
                                    todoHighlightId = todoHighlight
                                ))
                                
                                currentReminders.forEach { reminder ->
                                    if (!reminder.isTodo) {
                                        scheduler.scheduleSnooze(
                                            reminder.id,
                                            reminder.name,
                                            reminder.dose,
                                            reminder.time?.toString()
                                        )
                                    }
                                }
                                reminderManager.dismissAll()
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            val medId = it.getIntExtra("HIGHLIGHT_ID", -1)
            val medTime = it.getStringExtra("HIGHLIGHT_TIME")
            val todoId = it.getIntExtra("HIGHLIGHT_TODO_ID", -1)
            val navTodo = it.getBooleanExtra("NAVIGATE_TO_TODO", false)

            lifecycleScope.launch {
                if (medId != -1) {
                    navigationEvents.emit(NavEvent(medicationHighlights = listOf(medId to medTime)))
                } else if (todoId != -1) {
                    navigationEvents.emit(NavEvent(todoHighlightId = todoId))
                } else if (navTodo) {
                    navigationEvents.emit(NavEvent(navigateToTodo = true))
                }
            }
        }
    }
}
