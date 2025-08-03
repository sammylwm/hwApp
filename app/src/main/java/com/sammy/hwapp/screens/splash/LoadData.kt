package com.sammy.hwapp.screens.splash

import LogIo
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import com.sammy.hwapp.SharedPref
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
suspend fun loadData(context: Context){
    val sharedPref = SharedPref(context = context, "UserData")
    val email = sharedPref.get("email")
    val login = sharedPref.get("loginDn")
    val password = sharedPref.get("passwordDn")
    val className = sharedPref.get("class")

    val prefs = SharedPref(context, "app_usage")
    val openedToday = prefs.get("last_open_date")
    val isTodayOpen = LocalDate.now().toString() == openedToday

    var remaining = if (isTodayOpen) 2 else 4

    fun done() {
        remaining--
        if (remaining == 0) {
            return
        }
    }

    if (!isTodayOpen){
        val result = LogIo.getMarks(login, password)
        val marksArray = JSONArray(result)
        val cleanedMarks = JSONArray()

        for (i in 0 until marksArray.length()) {
            val item = marksArray.getJSONArray(i)
            val entry = JSONArray().apply {
                put(item.getString(0))
                put(item.getString(1))
                put(item.getInt(2))
            }
            cleanedMarks.put(entry)
        }
        val marksPref = SharedPref(context, "userMarks")
        marksPref.update(mapOf("marks" to cleanedMarks.toString()))
        done()

        val result1 = LogIo.getAllMarks(login, password)
        val rawData = JSONArray(result1)
        val avgMarks = JSONArray()

        for (i in 0 until rawData.length()) {
            val item = rawData.getJSONArray(i)
            val entry = JSONArray().apply {
                put(item.getString(0))
                put(item.getString(1))
                put(item.getJSONArray(2))
            }
            avgMarks.put(entry)
        }
        marksPref.update(mapOf("marks_all" to avgMarks.toString()))
        done()
    }
    val ifAdmin = LogIo.checkAdm(className, email).toIntOrNull()
    sharedPref.update(mapOf("ifAdmin" to ifAdmin.toString()))
    done()

    val membersRes = LogIo.getMembers(className)
    val json = JSONObject(membersRes)

    val owner = json.getString("owner")
    val admins = json.optJSONArray("admins") ?: JSONArray()
    sharedPref.update(mapOf("owner"  to owner, "admins" to admins.toString()))
    done()
}