package com.example.alzapp.data.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.alzapp.MainActivity
import com.example.alzapp.R
import com.example.alzapp.data.medicationReminder.MedicationRepository
import com.example.alzapp.data.medicationReminder.MedicationScheduler
import com.example.alzapp.data.notifications.local.SnoozeDao
import com.example.alzapp.data.todo.TodoRepository
import com.example.alzapp.data.todo.TodoScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

const val ALARM_CHANNEL_ID = "ALARM_CHANNEL_V1"
const val INFO_CHANNEL_ID = "INFO_CHANNEL_V2"

fun createNotificationChannels(context: Context) {
    val alarmChannel = NotificationChannel(
        ALARM_CHANNEL_ID, "Alarmy i Przypomnienia", NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Kanał dla krytycznych alarmów"
        enableLights(true)
        enableVibration(true)
        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        setSound(
            android.provider.Settings.System.DEFAULT_RINGTONE_URI,
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
    }

    val infoChannel = NotificationChannel(
        INFO_CHANNEL_ID, "Informacje i przeglądy", NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Kanał dla zwykłych powiadomień"
        enableLights(true)
        enableVibration(true)
        setSound(
            android.provider.Settings.System.DEFAULT_NOTIFICATION_URI,
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
    }

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(alarmChannel)
    notificationManager.createNotificationChannel(infoChannel)
}

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderManager: ActiveNotificationManager

    @Inject
    lateinit var todoRepository: TodoRepository

    @Inject
    lateinit var todoScheduler: TodoScheduler

    @Inject
    lateinit var medicationRepository: MedicationRepository

    @Inject
    lateinit var medicationScheduler: MedicationScheduler

    @Inject
    lateinit var snoozeDao: SnoozeDao

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationReceiver", "onReceive called with action: ${intent.action}")

        // reschedule alarms after device reboot
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    todoScheduler.rescheduleAllInternal()
                    medicationScheduler.rescheduleAllInternal()
                } finally {
                    pendingResult.finish()
                }
            }
            return
        }

        // handle incoming notification intents
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                handleNotificationIntent(context, intent)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun handleNotificationIntent(context: Context, intent: Intent) {
        val medId = intent.getIntExtra("MED_ID", -1)
        val todoId = intent.getIntExtra("TODO_ID", -1)
        val batchAction = intent.getStringExtra("BATCH_ACTION")

        when {
            // handle single med reminder
            medId != -1 -> {
                val medName = intent.getStringExtra("MED_NAME") ?: return
                val medDose = intent.getStringExtra("MED_DOSE") ?: ""
                val medTimeStr = intent.getStringExtra("MED_TIME")
                val isSnooze = intent.getBooleanExtra("IS_SNOOZE", false)

                val medTime = try {
                    if (!medTimeStr.isNullOrEmpty() && medTimeStr != "null") LocalTime.parse(
                        medTimeStr
                    ) else null
                } catch (e: Exception) {
                    null
                }

                if (medTime == null) {
                    Log.e("NotificationReceiver", "Invalid time for med: $medTimeStr")
                    return
                }

                val med = medicationRepository.getMedicationById(medId)
                val isTaken =
                    med?.takenDateTimes?.contains(LocalDateTime.of(LocalDate.now(), medTime))
                        ?: false

                if (!isTaken) {
                    // show reminder and schedule next snooze if not taken yet
                    handleMedicationReminder(
                        context,
                        medId,
                        medName,
                        medDose,
                        medTime,
                        medTimeStr,
                        isSnooze
                    )
                    medicationScheduler.scheduleSnooze(medId, medName, medDose, medTimeStr)
                } else {
                    // remove snooze if med was already taken
                    snoozeDao.deleteSnooze(medId, medTimeStr ?: "")
                }

                if (isSnooze) {
                    // if a snooze alarm -> delete it from the database after firing
                    snoozeDao.deleteSnooze(medId, medTimeStr ?: "")
                } else {
                    // schedule next snooze
                    med?.let { medicationScheduler.scheduleMedication(it) }
                }
            }

            // handle single task reminder
            todoId != -1 -> {
                val title = intent.getStringExtra("TODO_TITLE") ?: ""
                val desc = intent.getStringExtra("TODO_DESC") ?: ""
                val reminderMins = intent.getIntExtra("REMINDER_MINS", -1)
                val isActualTime = intent.getBooleanExtra("IS_ACTUAL_TIME", false)

                handleTodoReminder(context, todoId, title, desc, reminderMins, isActualTime)
            }

            // handle morning/evening batch review
            batchAction != null -> {
                handleBatchAction(context, batchAction)
            }
        }
    }

    private fun handleMedicationReminder(
        context: Context,
        medId: Int,
        medName: String,
        medDose: String,
        medTime: LocalTime?,
        medTimeStr: String?,
        isSnooze: Boolean
    ) {
        reminderManager.showReminder(medId, medName, medDose, medTime)
        val notificationId = if (isSnooze) medId + 1000000 else medId
        showAlarmNotification(
            context,
            "ALARM: $medName",
            "Dawka: $medDose",
            notificationId,
            medTimeStr,
            true
        )
    }

    private fun handleTodoReminder(
        context: Context,
        todoId: Int,
        title: String,
        desc: String,
        reminderMins: Int,
        isActualTime: Boolean
    ) {
        reminderManager.showTodoReminder(todoId, title, desc, reminderMins)

        val notificationTitle =
            if (isActualTime) "TERAZ: $title" else "Za $reminderMins min: $title"
        val notificationText =
            if (isActualTime) "Czas na Twoje zadanie." else "Zbliża się czas zadania. Przygotuj się."

        showAlarmNotification(
            context,
            notificationTitle,
            notificationText,
            todoId + (if (isActualTime) 2500000 else 2000000),
            null,
            isMedication = false,
            todoId = todoId
        )
    }

    private suspend fun handleBatchAction(context: Context, action: String) {
        val todayDate = LocalDate.now()
        val tasks = todoRepository.getTasksByDate(todayDate.toString())

        when (action) {
            "MORNING_REVIEW" -> {
                val count = tasks.size
                val notificationMessage = if (count > 0) {
                    "Masz dziś $count ${getTaskWord(count)} do wykonania. Zerknij na listę."
                } else {
                    "Nie masz dziś żadnych zaplanowanych zadań. Miłego dnia!"
                }

                showSimpleNotification(
                    context,
                    "Dzień dobry!",
                    notificationMessage,
                    3000001
                )
            }

            "EVENING_REVIEW" -> {
                // only notify if there are pending tasks left
                if (tasks.any { !it.isCompleted }) {
                    showSimpleNotification(
                        context,
                        "Dobry wieczór!",
                        "Zostało Ci jeszcze kilka zadań na dziś. Zerknij na listę.",
                        3000002
                    )
                }
            }
        }
    }

    // util fun; polish pluralization
    private fun getTaskWord(count: Int): String {
        return when {
            count == 1 -> "zadanie"
            count % 10 in 2..4 && (count % 100 !in 12..14) -> "zadania"
            else -> "zadań"
        }
    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        text: String,
        id: Int,
        timeStr: String?,
        isMedication: Boolean,
        todoId: Int? = null
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // intent to open app and highlight specific item
        val fullScreenIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            if (isMedication) {
                putExtra("HIGHLIGHT_ID", if (id >= 1000000) id - 1000000 else id)
                putExtra("HIGHLIGHT_TIME", timeStr)
            } else {
                putExtra("HIGHLIGHT_TODO_ID", todoId)
            }
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, id, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(if (isMedication) R.drawable.icon_medications else R.drawable.icon_todo)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setOngoing(true)
            .setAutoCancel(true)
            .setSound(android.provider.Settings.System.DEFAULT_RINGTONE_URI)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(R.drawable.icon_checked, "OTWÓRZ APLIKACJĘ", fullScreenPendingIntent)

        val notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_INSISTENT
        notificationManager.notify(id, notification)
    }

    private fun showSimpleNotification(context: Context, title: String, text: String, id: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("NAVIGATE_TO_TODO", true)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, INFO_CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_todo)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(pendingIntent)

        notificationManager.notify(id, builder.build())
    }
}