package com.workalarm.workmanalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.work.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object ScheduleAlarm {

    fun scheduleAlarm(context: Context) {
        Log.d("AlarmWorker", "scheduleAlarm Called")
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 1 * 60 * 1000,
            PendingIntent.getBroadcast(
                context,
                100,
                Intent(context, NotificationAlarmReceiver::class.java).apply {
                    putExtra("title", "Triggered at ${
                        SimpleDateFormat("dd MMMM YYYY, HH:mm:ss").format(
                            Calendar.getInstance().time)}")
                },
                PendingIntent.FLAG_UPDATE_CURRENT or Intent.FILL_IN_DATA
            )
        )
    }

    fun addTask(context: Context) {

        val constraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val alarmRequest = PeriodicWorkRequest.Builder(
            ServiceWorker::class.java,
            15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag(AlarmWorker::class.java.simpleName)
            .build()


        WorkManager.getInstance(context).
            enqueueUniquePeriodicWork(AlarmWorker::class.java.simpleName, ExistingPeriodicWorkPolicy.KEEP, alarmRequest)

    }
}