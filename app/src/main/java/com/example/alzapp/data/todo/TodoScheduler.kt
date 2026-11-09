package com.example.alzapp.data.todo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.alzapp.data.todo.local.TodoTaskEntity
import com.example.alzapp.data.notifications.NotificationReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

// used for batch reviews; negative numbers will never be in conflict with numbers from getRequestCode since those are always positive
private const val MORNING_REVIEW_CODE = -1
private const val EVENING_REVIEW_CODE = -2
@Singleton
class TodoScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val todoRepository: TodoRepository
) {
    // scope for background operations like rescheduling alarms
    private val schedulerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // even for on-time reminders, odd for in-advance reminders
    private fun getRequestCode(todoId: Int, isActualTime: Boolean): Int {
        return if (isActualTime) todoId * 2 else todoId * 2 + 1
    }

    private fun canScheduleExact(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    // util function to not repeat the same code
    private fun scheduleAlarm(task: TodoTaskEntity, dateTime: LocalDateTime, isActualTime: Boolean, reminderMins: Int? = null) {
        val triggerTime = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (triggerTime <= System.currentTimeMillis()) return // don't schedule if time has already passed

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("TODO_ID", task.id)
            putExtra("TODO_TITLE", task.title)
            putExtra("TODO_DESC", task.taskDescription)
            putExtra("IS_ACTUAL_TIME", isActualTime)
            reminderMins?.let { putExtra("REMINDER_MINS", it) }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            getRequestCode(task.id, isActualTime),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        setAlarm(triggerTime, pendingIntent)
        Log.d("TodoScheduler", "Scheduled ${if(isActualTime) "ON-TIME" else "ADVANCE"} for ${task.title} at $dateTime")
    }

    // main entry point to set up notifications for a specific task
    fun scheduleTaskReminder(task: TodoTaskEntity) {
        Log.d("TodoScheduler", "Processing schedule for task: ${task.title} (ID: ${task.id})")
        val taskTime = task.time ?: return // immediately return if there's no specified time for the task
        val taskDateTime = LocalDateTime.of(task.date.toLocalDate(), taskTime)

        // schedule an in-advance reminder (if applicable)
        task.reminderMinutesBefore?.let { minutes ->
            if (minutes > 0) {
                val trigger = taskDateTime.minusMinutes(minutes.toLong())
                scheduleAlarm(task, trigger, isActualTime = false, reminderMins = minutes)
            }
        }

        // schedule on-time reminder
        scheduleAlarm(task, taskDateTime, isActualTime = true)
    }

    // utils for fun scheduleBatchReviews()
    private fun scheduleReview(action: String, hour: Int, requestCode: Int) {
        val now = LocalDateTime.now()
        var scheduledTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, 0))

        if (scheduledTime.isBefore(now)) {
            scheduledTime = scheduledTime.plusDays(1)
        }

        val triggerTime = scheduledTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val triggerDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(triggerTime), ZoneId.systemDefault())

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("BATCH_ACTION", action)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        setAlarm(triggerTime, pendingIntent)
        Log.d("TodoScheduler", "Scheduled batch review $action for $triggerDateTime")
    }

    // set up daily morning + evening reminders to remind the user to check the app
    private fun scheduleBatchReviews() {
        scheduleReview("MORNING_REVIEW", 8, MORNING_REVIEW_CODE)
        scheduleReview("EVENING_REVIEW", 17, EVENING_REVIEW_CODE)
    }

    // final step to set up reminder; checks permissions
    private fun setAlarm(triggerTime: Long, pendingIntent: PendingIntent) {
        try {
            if (canScheduleExact()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } catch (e: Exception) {
            Log.e("TodoScheduler", "Failed to schedule alarm", e)
        }
    }

    // cancel all scheduled alarms for a specific task
    fun cancelTaskReminder(task: TodoTaskEntity) {
        Log.d("TodoScheduler", "Cancelling reminders for task: ${task.title} (ID: ${task.id})")
        // cancel an in-advance reminder
        cancelSpecificAlarm(task.id, isActualTime = false)
        // cancel the on-time reminder
        cancelSpecificAlarm(task.id, isActualTime = true)
    }

    // util for cancelTaskReminder
    private fun cancelSpecificAlarm(todoId: Int, isActualTime: Boolean) {
        val requestCode = getRequestCode(todoId, isActualTime)
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d("TodoScheduler", "Cancelled alarm for todoId=$todoId (isActualTime=$isActualTime)")
        }
    }

    // reschedule all reminders (batch reviews + uncompleted tasks)
    fun rescheduleAll() {
        Log.d("TodoScheduler", "Starting rescheduleAll for tasks")
        // reschedule batch reviews
        scheduleBatchReviews()

       schedulerScope.launch {
            rescheduleAllInternal()
        }
    }

    // reschedule reminder for uncompleted tasks
    suspend fun rescheduleAllInternal() {
        Log.d("TodoScheduler", "Running rescheduleAllInternal")
        try {
            val tasks = todoRepository.getAllTasksSync()

            // reschedule all uncompleted tasks for today
            tasks.filter { !it.isCompleted }.forEach { task -> scheduleTaskReminder(task) }
        } catch (e: Exception) {
            Log.e("TodoScheduler", "Error during rescheduling", e)
        }
    }
}
