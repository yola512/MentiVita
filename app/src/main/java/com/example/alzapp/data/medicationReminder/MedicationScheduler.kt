package com.example.alzapp.data.medicationReminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.alzapp.data.notifications.NotificationReceiver
import com.example.alzapp.data.notifications.local.SnoozeDao
import com.example.alzapp.data.medicationReminder.local.MedicationEntity
import com.example.alzapp.data.notifications.local.SnoozeEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class MedicationScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val medicationRepository: MedicationRepository,
    private val snoozeDao: SnoozeDao,
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val schedulerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // unique code for each reminder based on medId and time
    private fun getRequestCode(medId: Int, timeHash: Int, isSnooze: Boolean = false): Int {
        val base = abs((medId * 31) + timeHash)
        return if (isSnooze) base + 1000000 else base
    }

    // checks if the app has permission to set exact alarms
    private fun canScheduleExact(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    // plans reminders for medication based on its scheduled times
    fun scheduleMedication(medication: MedicationEntity) {
        Log.d("MedicationScheduler", "Processing schedule for medication ${medication.name} (ID: ${medication.id})")
        medication.takingTimes.forEach { dateTime ->
            val localTime = dateTime.toLocalTime()
            findNextValidDateTime(medication, localTime)?.let { date ->
                val trigger = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                scheduleAlarm(medication.id, medication.name, medication.dose, localTime.toString(), trigger)
                Log.d("MedicationScheduler", "Scheduled alarm for ${medication.name} at $date")
            }
        }
    }

    // util function for scheduling/rescheduling alarms
    private fun scheduleAlarm(
        id: Int,
        name: String,
        dose: String,
        timeStr: String,
        triggerTime: Long,
        isSnooze: Boolean = false
    ) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("MED_ID", id)
            putExtra("MED_NAME", name)
            putExtra("MED_DOSE", dose)
            putExtra("MED_TIME", timeStr)
            if (isSnooze) putExtra("IS_SNOOZE", true)
        }

        val requestCode = getRequestCode(id, timeStr.hashCode(), isSnooze)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        setAlarm(triggerTime, pendingIntent)
    }

    // calculates next date a medication should be taken (considering end date and specific days of the week)
    private fun findNextValidDateTime(medication: MedicationEntity, localTime: LocalTime): LocalDateTime? {
        val now = LocalDateTime.now()
        val startDate = medication.startDate
        val endDate = medication.endDate
        val takingDays = medication.takingDays

        // start checking from today or the start date, whichever is later
        var checkDate = if (startDate.isAfter(now.toLocalDate())) startDate else now.toLocalDate()
        
        // check for the next 365 days maximum to avoid infinite loop
        repeat(365) {
            if (checkDate.isAfter(endDate)) return null

            val isTakingDay = takingDays.contains("Codziennie") || 
                             takingDays.contains(dayOfWeekToPolish(checkDate.dayOfWeek))

            if (isTakingDay) {
                val scheduledDateTime = LocalDateTime.of(checkDate, localTime)
                if (scheduledDateTime.isAfter(now)) {
                    return scheduledDateTime
                }
            }
            checkDate = checkDate.plusDays(1)
        }
        return null
    }

    private fun dayOfWeekToPolish(day: DayOfWeek): String {
        return when (day) {
            DayOfWeek.MONDAY -> "Poniedziałek"
            DayOfWeek.TUESDAY -> "Wtorek"
            DayOfWeek.WEDNESDAY -> "Środa"
            DayOfWeek.THURSDAY -> "Czwartek"
            DayOfWeek.FRIDAY -> "Piątek"
            DayOfWeek.SATURDAY -> "Sobota"
            DayOfWeek.SUNDAY -> "Niedziela"
        }
    }

    // schedules 10-minute snooze and saves it to database
    fun scheduleSnooze(medicationId: Int, name: String, dose: String, timeStr: String?) {
        Log.d("MedicationScheduler", "Attempting to schedule snooze for $name at $timeStr")

        val triggerTime = System.currentTimeMillis() + (10 * 60 * 1000)
        val safeTimeStr = timeStr ?: ""

        scheduleAlarm(medicationId, name, dose, safeTimeStr, triggerTime, isSnooze = true)

        // persist snooze to database
        schedulerScope.launch {
            snoozeDao.insertSnooze(
                SnoozeEntity(
                    medId = medicationId,
                    medName = name,
                    medDose = dose,
                    medTimeStr = timeStr ?: "",
                    triggerTime = triggerTime
                )
            )
        }
    }

    // sets actual alarm with exact timing
    private fun setAlarm(triggerTime: Long, pendingIntent: PendingIntent) {
        try {
            if (canScheduleExact()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                // fallback for missing permission
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } catch (e: SecurityException) {
            Log.e("MedicationScheduler", "SecurityException: Exact alarm permission not granted", e)
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        } catch (e: Exception) {
            Log.e("MedicationScheduler", "Failed to schedule alarm", e)
        }
    }

    // cancels snooze and removes it from the database
    fun cancelSnooze(medicationId: Int, timeStr: String?) {
        val timeHash = timeStr?.hashCode() ?: 0
        val requestCode = getRequestCode(medicationId, timeHash, isSnooze = true)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d("MedicationScheduler", "Snooze cancelled for medId=$medicationId")
        }

        // remove from database
        schedulerScope.launch {
            snoozeDao.deleteSnooze(medicationId, timeStr ?: "")
        }
    }

    // cancels all reminders (regular + snooze) related to medication
    fun cancelMedication(medication: MedicationEntity) {
        medication.takingTimes.forEach { time ->
            val timeStr = time.toLocalTime().toString()
            val requestCode = getRequestCode(medication.id, timeStr.hashCode(), isSnooze = false)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, NotificationReceiver::class.java),
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                Log.d("MedicationScheduler", "Cancelled reminder for medication ${medication.name} at $time")
            }
            cancelSnooze(medication.id, time.toLocalTime().toString())
        }
    }

    fun rescheduleAll() {
        schedulerScope.launch {
            rescheduleAllInternal()
        }
    }

    // reschedules all med reminders and pending snoozes
    suspend fun rescheduleAllInternal() {
        try {
            val medications = medicationRepository.getAllMedicationsSync()
            medications.forEach { medication -> scheduleMedication(medication) }

            // restore pending snoozes
            val now = System.currentTimeMillis()
            val pendingSnoozes = snoozeDao.getAllSnoozes()
            
            pendingSnoozes.forEach { snooze ->
                // if the snooze is still in the future or within the 60-minute grace window
                val isRecent = (now - snooze.triggerTime) < (60 * 60 * 1000)
                
                if (snooze.triggerTime > now || isRecent) {
                    // if the trigger time is in the past (but recent), fire it in 10 seconds to give system time to boot up
                    val triggerTime = if (snooze.triggerTime > now) snooze.triggerTime else now + 10000
                    scheduleAlarm(snooze.medId, snooze.medName, snooze.medDose, snooze.medTimeStr, triggerTime, isSnooze = true)
                } else {
                    snoozeDao.deleteSnooze(snooze.medId, snooze.medTimeStr)
                }
            }
        } catch (e: Exception) {
            Log.e("MedicationScheduler", "Error during rescheduling all medications", e)
        }
    }
}
