package com.workalarm.workmanalarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.concurrent.TimeUnit


class SchedulerService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            when(action) {
                START_FOREGROUND -> {
                    startForeground()
                }
                STOP_FOREGROUND -> {
                    stopService()
                }
            }
        }
        return START_STICKY
    }

    private fun stopService() {
        Log.d("AlarmWorker", "SchedulerService :: stopService Called")
        // Stop foreground service and remove the notification.
        stopForeground(true);
        // Stop the foreground service.
        stopSelf();
    }

    private fun startForeground() {
        Log.d("AlarmWorker", "SchedulerService :: startForeground Called")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CHANNEL_ID, "Background Scheduler Service")
        } else {

            // Create notification default intent.
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            // Create notification builder.
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)

            // Make notification show big text.
            val bigTextStyle = NotificationCompat.BigTextStyle()
            bigTextStyle.setBigContentTitle("Checking new sessions")
            bigTextStyle.bigText("Your education is our priority")
            // Set big text style.
            builder.setStyle(bigTextStyle)
            builder.setWhen(System.currentTimeMillis())
            builder.setSmallIcon(R.mipmap.ic_launcher_round)
            val largeIconBitmap =
                BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background)
            builder.setLargeIcon(largeIconBitmap)
            // Make the notification max priority.
            builder.priority = NotificationManagerCompat.IMPORTANCE_HIGH
            // Make head-up notification.
            builder.setFullScreenIntent(pendingIntent, true)

            // Add Play button intent in notification.
            val playIntent = Intent(this, SchedulerService::class.java)
            playIntent.action = STOP_FOREGROUND
            val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0)
            val playAction = NotificationCompat.Action(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Stop",
                pendingPlayIntent
            )
            builder.addAction(playAction)


            // Build the notification.
            val notification = builder.build()

            // Start foreground service.
            startForeground(1, notification)
        }
        scheduleTask()
    }

    private fun scheduleTask() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10))
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Log.d("AlarmWorker", "SchedulerService :: scheduleTask Called")
        ScheduleAlarm.scheduleAlarm(this)
        stopService()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String) {
        val resultIntent = Intent(this, MainActivity::class.java)
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(resultIntent)
        val resultPendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val chan =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Checking for new sessions")
            .setContentText("Your education is our priority")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(resultPendingIntent) //intent
            .build()
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, notificationBuilder.build())
        startForeground(1, notification)
    }

    companion object {

        private const val START_FOREGROUND = "Start_Foreground"
        private const val STOP_FOREGROUND = "Stop_Foreground"
        private const val CHANNEL_ID = "SchedulerService"

        fun startSchedulerService(context: Context) = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(
                Intent(context, SchedulerService::class.java).apply {
                    action = START_FOREGROUND
                }
            )
        } else {
            context.startService(
                Intent(context, SchedulerService::class.java).apply {
                    action = START_FOREGROUND
                }
            )
        }
    }
}