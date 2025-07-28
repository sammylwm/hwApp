package com.sammy.hwapp.screens.main.fragments.homework

import LogIo
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sammy.hwapp.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HwViewModel : ViewModel() {
    private val _isSharedPref = MutableStateFlow(false)
    val isSharedPref: StateFlow<Boolean> = _isSharedPref

    private val today: String = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
    private val _selectedDate = MutableStateFlow<String?>(today)
    val selectedDate: StateFlow<String?> = _selectedDate

    private val _showDatePicker = MutableStateFlow(false)
    val ifShowDatePicker: StateFlow<Boolean> = _showDatePicker

    private val _addHw = MutableStateFlow<String?>(null)
    val ifAddHw: StateFlow<String?> = _addHw

    private val _addHwState = MutableStateFlow<Boolean>(false)
    val addHwState: StateFlow<Boolean> = _addHwState

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _subjects = mutableStateListOf<String>()
    val subjects: SnapshotStateList<String> = _subjects

    private val _homeworks = mutableStateListOf<String>()
    val homeworks: SnapshotStateList<String> = _homeworks

    private val _className = MutableStateFlow<String?>(null)
    val className: StateFlow<String?> = _className

    private val _ifAdmin = MutableStateFlow<String?>(null)
    val ifAdmin: StateFlow<String?> = _ifAdmin

    fun showDatePicker(bool: Boolean){
        _showDatePicker.value = bool
    }

    fun setSelectedDate(date: String){
        _selectedDate.value = date
    }

    fun clearSharedPref(context: Context){
        val sharedPref = SharedPref(context, "UserData")
        sharedPref.clear()
    }

    fun loadSharedPref(context: Context){
        val sharedPref = SharedPref(context, "UserData")
        _className.value = sharedPref.get("class")
        _ifAdmin.value = sharedPref.get("ifAdmin")
        _isSharedPref.value = true
    }

    fun addHw(homework: String, subject: String, selectedDate: String, className: String){
        viewModelScope.launch {
            val result = LogIo.addHw(homework, subject, selectedDate, className).toIntOrNull()
            _addHw.value = if (result == 1) "Успешно добавлено!" else "Ошибка при добавлении"
            _addHwState.value = true
        }
    }

    fun load(date: String, className: String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = LogIo.getHw(date, className)
            val jsonArray = JSONArray(result ?: "[]")
            for (i in 0 until jsonArray.length()) {
                val entry = jsonArray.getString(i)
                val parts = entry.split(".", limit = 2)
                if (parts.size == 2) {
                    _subjects.add(parts[0])
                    val rawHomework = parts[1]
                    val cleaned = rawHomework
                        .replace("[", "")
                        .replace("]", "")
                        .replace("'", "")
                        .split(", ")
                        .filter { it.isNotBlank() }
                    _homeworks.add(cleaned.joinToString("\n")
                        .ifEmpty { "Нет домашнего задания" })
                }
            }
            _isLoading.value = false
            _isLoaded.value = true
        }
    }
}