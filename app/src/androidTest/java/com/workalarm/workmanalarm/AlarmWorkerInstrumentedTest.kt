package com.workalarm.workmanalarm

import android.content.Context
import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.*
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class AlarmWorkerInstrumentedTest {

    private lateinit var myContext: Context
    @Before
    fun setUp() {
        myContext = InstrumentationRegistry.getTargetContext()
        val config = Configuration.Builder()
            // Set log level to Log.DEBUG to make it easier to debug
            .setMinimumLoggingLevel(Log.DEBUG)
            // Use a SynchronousExecutor here to make it easier to write tests
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(myContext, config)
    }

    @Test
    @Throws(Exception::class)
    fun testPeriodicWork() {
        // Define input data
        val input = Data.Builder().put("key_1", 1).put("key_2", 2).build()

        // Create request
        val request = PeriodicWorkRequest.Builder(ServiceWorker::class.java, 15, TimeUnit.MINUTES)
            .setInputData(input)
            .build()

        val workManager = WorkManager.getInstance(myContext)
        val testDriver = WorkManagerTestInitHelper.getTestDriver()
        // Enqueue and wait for result.
        workManager.enqueue(request).result.get()
        // Tells the testing framework the period delay is met
        testDriver?.setPeriodDelayMet(request.id)
        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()
        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))

        /*val outputData = workInfo.outputData
        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        assertThat(outputData, `is`(input))*/
    }
}