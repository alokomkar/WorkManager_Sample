package com.workalarm.workmanalarm

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

object PersistenceLogger {

    private const val KEY_LOGS = "key_logs"
    private val dateFormat by lazy { SimpleDateFormat("dd-MMM-yyyy :: HH:mm:ss", Locale.ENGLISH) }

    fun log(context: Context, className: String, description: String) {
        Log.d(className, description)
        context.getSharedPreferences(PersistenceLogger::class.java.simpleName, Context.MODE_PRIVATE).edit().apply {
            val logs = fetchLogs(context)
            logs.add("${dateFormat.format(Date())} :: $className ::: $description")
            putStringSet(KEY_LOGS, logs.toHashSet())
        }.apply()
    }

    fun fetchLogs(context: Context): MutableList<String> {
         val logsSet = context.getSharedPreferences(PersistenceLogger::class.java.simpleName, Context.MODE_PRIVATE).getStringSet(
            KEY_LOGS, HashSet())
        return (logsSet ?: mutableListOf<String>()).toMutableList()
    }

}