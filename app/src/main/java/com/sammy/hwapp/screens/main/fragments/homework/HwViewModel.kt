package com.sammy.hwapp.screens.main.fragments.homework

import LogIo
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sammy.hwapp.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UiState(
    val isSharedPref: Boolean = false,
    val ifShowDatePicker: Boolean = false,
    val ifAddHw: String? = null,
    val addHwState: Boolean = false,
    val isLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val subjects: List<String> = listOf(),
    val homeworks: List<String> = listOf(),
    val className: String? = null,
    val ifAdmin: Boolean = false,
)

class HwViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    init {
    }

    fun showDatePicker(bool: Boolean){
        _uiState.value = _uiState.value.copy(ifShowDatePicker = bool)
    }
    fun clearSharedPref(context: Context){
        val sharedPref = SharedPref(context, "UserData")
        sharedPref.clear()
    }
    fun loadSharedPref(context: Context){
        val sharedPref = SharedPref(context, "UserData")
        _uiState.value = _uiState.value.copy(
            className = sharedPref.get("class"),
            ifAdmin = sharedPref.get("ifAdmin").toBoolean(),
            isSharedPref = true
        )
    }
    fun addHw(homework: String, subject: String, selectedDate: String, className: String){
        viewModelScope.launch {
            val result = LogIo.addHw(homework, subject, selectedDate, className).toIntOrNull()
            _uiState.value = _uiState.value.copy(
                ifAddHw = if (result == 1) "Успешно добавлено!" else "Ошибка при добавлении",
                addHwState = true
            )
        }
    }
    fun load(date: String, className: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = LogIo.getHw(date, className)
            val jsonArray = JSONArray(result ?: "[]")
            val subjectList = emptyList<String>().toMutableStateList()
            val homeworkList = emptyList<String>().toMutableStateList()
            for (i in 0 until jsonArray.length()) {
                val entry = jsonArray.getString(i)
                val parts = entry.split(".", limit = 2)
                if (parts.size == 2) {
                    subjectList.add(parts[0])
                    val rawHomework = parts[1]
                    val cleaned = rawHomework
                        .replace("[", "")
                        .replace("]", "")
                        .replace("'", "")
                        .split(", ")
                        .filter { it.isNotBlank() }
                    homeworkList.add(cleaned.joinToString("\n")
                        .ifEmpty { "Нет домашнего задания" })
                }
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isLoaded = true,
                subjects = subjectList,
                homeworks = homeworkList
            )
        }
    }

}