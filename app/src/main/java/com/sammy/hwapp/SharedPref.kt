package com.sammy.hwapp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import org.json.JSONArray

class SharedPref(context: Context, name: String) {
    private val sharedPref = context.getSharedPreferences(name, MODE_PRIVATE)!!
    fun update(fields: Map<String, String>) {
        sharedPref.edit {
            for ((key, value) in fields) {
                putString(key, value)
            }
            apply()
        }
    }
    fun get(name: String): String = sharedPref.getString(name, "") ?: ""
    fun contains(name: String): Boolean = sharedPref.contains(name)

    fun getArray(name: String): List<String> {
        val res = sharedPref.getString(name, "") ?: ""
        return try {
            val jsonArray = JSONArray(res)
            List(jsonArray.length()) { index -> jsonArray.optString(index) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clear(){
        sharedPref.edit{clear()}
    }
}