package com.workalarm.workmanalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val constraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val alarmRequest = PeriodicWorkRequest.Builder(
            AlarmWorker::class.java,
            20, TimeUnit.MINUTES,
            5,
            TimeUnit.MINUTES)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .addTag(AlarmWorker::class.java.simpleName)
            .build()

        val oneTimeRequest = OneTimeWorkRequest.Builder(AlarmWorker::class.java).build()

        WorkManager.getInstance(this).apply {
            enqueue(oneTimeRequest)
            enqueue(alarmRequest)
        }


    }
}