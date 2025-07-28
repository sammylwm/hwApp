package com.sammy.hwapp.screens.main.fragments.grades

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.sammy.hwapp.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray
import java.util.random.RandomGenerator.StreamableGenerator

data class GradeEntry(
    val date: String,
    val subject: String,
    val grade: String
)

class GradesViewModel: ViewModel() {
    private val _grades = mutableStateListOf<GradeEntry>()
    val grades: SnapshotStateList<GradeEntry> = _grades

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    fun loadGrades(context: Context){
        val sharedPref = SharedPref(context, "userMarks")
        val marksJson = sharedPref.get("marks")
        val jsonArray = JSONArray(marksJson)
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONArray(i)
            val date = item.getString(0)
            val subject = item.getString(1)
            val grade = item.getString(2)
            _grades.add(GradeEntry(date, subject, grade))
        }
        _isLoaded.value = true
    }
}


