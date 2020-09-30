package com.workalarm.workmanalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import java.text.SimpleDateFormat
import java.util.*

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

    fun scheduleAlarmClock(context: Context) {
        Log.d("AlarmWorker", "scheduleAlarmClock Called")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            100,
            Intent(context, NotificationAlarmReceiver::class.java).apply {
                putExtra("title", "Triggered at ${Calendar.getInstance().time}")
            },
            PendingIntent.FLAG_UPDATE_CURRENT or Intent.FILL_IN_DATA
        )
        val alarm = AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 1 * 60 * 1000,
            pendingIntent)
        alarmManager.setAlarmClock(alarm, pendingIntent)
    }
}