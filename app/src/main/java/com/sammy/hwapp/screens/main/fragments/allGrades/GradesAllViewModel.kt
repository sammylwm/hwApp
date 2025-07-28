package com.sammy.hwapp.screens.main.fragments.allGrades

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.sammy.hwapp.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray

class GradesAllViewModel: ViewModel() {
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    private val _subjects = mutableStateListOf<String>()
    val subjects: SnapshotStateList<String> = _subjects

    private val _grades = mutableStateListOf<String>()
    val grades: SnapshotStateList<String> = _grades

    private val _marks = mutableStateListOf<List<String>>()
    val marks: SnapshotStateList<List<String>> = _marks

    fun load(context: Context){
        val prefs = SharedPref(context, "userMarks")
        val jsonString = prefs.get("marks_all")
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val innerArray = jsonArray.getJSONArray(i)
            val subject = innerArray.getString(0)
            val avg = innerArray.getString(1)
            val grades = innerArray.getJSONArray(2)

            val gradeStrings = List(grades.length()) { j ->
                grades.getString(j)
            }
            _subjects.add(subject)
            _grades.add(avg)
            _marks.add(gradeStrings)
        }
        _isLoaded.value = true
    }
}