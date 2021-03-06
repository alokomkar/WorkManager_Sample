package com.workalarm.workmanalarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


class NotificationAlarmReceiver: BroadcastReceiver() {

    companion object {
        const val ALARM_ACTION = "com.workalarm.workmanalarm.action.ALARM"
        private const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {

            context ?: return

            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "workAlarm:NotificationAlarmReceiver")
            wakelock.acquire(3000)

            val title = intent.getStringExtra("title")

            val notification = NotificationCompat.Builder(context, "workAlarm")
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText("Testing")
                .setAutoCancel(false)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context, 101, Intent(
                            context,
                            MainActivity::class.java
                        ), PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .setChannelId("workAlarm")

            val notificationManager = NotificationManagerCompat.from(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val CHANNEL_ID = "workAlarm"
                val name: CharSequence = "workAlarm"
                val Description = "This is my channel"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
                mChannel.description = Description
                mChannel.enableLights(true)
                mChannel.lightColor = Color.RED
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mChannel.setShowBadge(false)
                notificationManager.createNotificationChannel(mChannel)
            }

            notificationManager
                .notify(1011, notification.build())

            PersistenceLogger.log(context,"AlarmWorker", "AlarmReceiver : onReceive")

        }

    }
}