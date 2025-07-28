package com.sammy.hwapp.screens.main.fragments.allGrades

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.sammy.hwapp.SharedPref
import com.sammy.hwapp.screens.main.fragments.homework.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

data class GradesAllUiState(
    val isLoaded: Boolean = false,
    val subjects: List<String> = emptyList(),
    val grades: List<String> = emptyList(),
    val marks: List<List<String>> = emptyList(),

)


class GradesAllViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(GradesAllUiState())
    val uiState: StateFlow<GradesAllUiState> = _uiState.asStateFlow()

    fun load(context: Context){
        val prefs = SharedPref(context, "userMarks")
        val jsonString = prefs.get("marks_all")
        val jsonArray = JSONArray(jsonString)
        val subjectList = emptyList<String>().toMutableStateList()
        val gradesList = emptyList<String>().toMutableStateList()
        val marksList = emptyList<List<String>>().toMutableStateList()

        for (i in 0 until jsonArray.length()) {
            val innerArray = jsonArray.getJSONArray(i)
            val subject = innerArray.getString(0)
            val avg = innerArray.getString(1)
            val grades = innerArray.getJSONArray(2)

            val gradeStrings = List(grades.length()) { j ->
                grades.getString(j)
            }
            subjectList.add(subject)
            gradesList.add(avg)
            marksList.add(gradeStrings)
        }
        _uiState.value = _uiState.value.copy(
            subjects = subjectList,
            grades = gradesList,
            marks = marksList,
            isLoaded = true
        )
    }
}