package com.workalarm.workmanalarm

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception

class ServiceWorker(private val context: Context, workerParameters: WorkerParameters): Worker(
    context,
    workerParameters
)  {

    override fun doWork(): Result {
        try {
            SchedulerService.startSchedulerService(context)
        } catch (e: Exception) {
            return Result.retry()
        }
        return Result.success()
    }
}