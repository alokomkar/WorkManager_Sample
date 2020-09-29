package com.workalarm.workmanalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

class AlarmWorker(private val context: Context, workerParameters: WorkerParameters): Worker(
    context,
    workerParameters
) {

    override fun doWork(): Result {
        scheduleAlarm(context)
        return Result.success()
    }

    private fun scheduleAlarm(context: Context) {
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            AlarmManager.RTC_WAKEUP,
            5 * 60 * 1000,
            PendingIntent.getBroadcast(
                context,
                100,
                Intent(NotificationAlarmReceiver.ALARM_ACTION).apply {
                    putExtra("title", "Triggered at ${Calendar.getInstance().time}")
                },
                PendingIntent.FLAG_UPDATE_CURRENT or Intent.FILL_IN_DATA
            )
        )
    }
}