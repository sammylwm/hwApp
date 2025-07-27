package com.sammy.hwapp

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

data class GradeEntry(
    val date: String,
    val subject: String,
    val grade: String
)

class MyViewModel: ViewModel() {
    private val _tasks = getWellnessTasks().toMutableStateList()
    val tasks: List<GradeEntry>
        get() = _tasks
}

private fun getWellnessTasks() = List(1) { i -> GradeEntry("10.09.2008",
    "алгебра", "5") }