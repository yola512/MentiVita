package com.example.alzapp

import android.app.Application
import com.example.alzapp.data.medicationReminder.MedicationScheduler
import com.example.alzapp.data.notifications.createNotificationChannels
import com.example.alzapp.data.todo.TodoScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AlzApp : Application() {
    
    @Inject
    lateinit var todoScheduler: TodoScheduler

    @Inject
    lateinit var medicationScheduler: MedicationScheduler

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels(this)
        todoScheduler.rescheduleAll()
        medicationScheduler.rescheduleAll()
    }
}
