package com.workalarm.workmanalarm

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScheduleAlarm.addTask(this)
        checkAndDisplayLogs()
    }

    private fun checkAndDisplayLogs() {
        object : AsyncTask<Void?, Void?, MutableList<String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                progressBar.visibility = View.VISIBLE
            }
            override fun doInBackground(vararg params: Void?): MutableList<String> {
                val logs = PersistenceLogger.fetchLogs(this@MainActivity)
                logs.sort()
                logs.reverse()
                return logs
            }

            @SuppressLint("StaticFieldLeak")
            override fun onPostExecute(result: MutableList<String>?) {
                super.onPostExecute(result)
                progressBar.visibility = View.GONE
                result ?: return
                var log = ""
                for(logs in result) {
                    log += if(log.isBlank()) logs else "\n\n" + logs
                }
                tvLogs.setText(log)
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }
}