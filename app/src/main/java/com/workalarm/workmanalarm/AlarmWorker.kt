package com.workalarm.workmanalarm

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class AlarmWorker(private val context: Context, workerParameters: WorkerParameters): Worker(
    context,
    workerParameters
) {

    override fun doWork(): Result {
        ScheduleAlarm.scheduleAlarm(context)
        return Result.success()
    }

}