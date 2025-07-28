package com.sammy.hwapp.screens.main.fragments.homework

import AddHomeworkDialog
import DatePickerModal
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sammy.hwapp.SharedPref
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val lessonTimes = listOf(
    "08:00\n08:40",
    "08:50\n09:30",
    "09:40\n10:20",
    "10:30\n11:10",
    "11:20\n12:00",
    "12:10\n12:50",
    "13:00\n13:40",
    "13:50\n14:30"
)

@Composable
fun HwFragment(view: HwViewModel = viewModel()) {
    val isLoaded by view.isLoaded.collectAsState()
    val addHwState by view.addHwState.collectAsState()
    val isAddHw by view.ifAddHw.collectAsState()
    val isSharedPref by view.isSharedPref.collectAsState()
    val isLoading by view.isLoading.collectAsState()
    val className by view.className.collectAsState()
    val ifAdmin by view.ifAdmin.collectAsState()
    val context = LocalContext.current
    val date = "28.07.2025"
    val subjects = view.subjects
    val homeworks = view.homeworks

    if (addHwState){
        Toast.makeText(
            context,
            isAddHw,
            Toast.LENGTH_SHORT
        ).show()
    }

    var showDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        view.loadSharedPref(context)
    }
    LaunchedEffect(isSharedPref, className) {
        if (isSharedPref && !className.isNullOrBlank()) {
            view.load(date, className!!, context)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
            ) {
                itemsIndexed(subjects) { index, subject ->
                    val time = lessonTimes.getOrNull(index) ?: ""
                    SubjectCard(subject, time, homeworks.getOrNull(index) ?: "")
                }
            }
        }
        if (ifAdmin == "1") {
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 10.dp, end = 10.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }
    if (showDialog) {
        AddHomeworkDialog(
            onDismiss = { showDialog = false },
            onSubmit = { subject, homework, selDate ->
                showDialog = false
                view.addHw(homework, subject, selDate, className.toString())
            },
            selectedDate = selectedDate,
            onDateClick = { showDatePicker = true }
        )
    }
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    selectedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(it))
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubjectCard(
    subject: String,
    time: String,
    homework: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = subject,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = time,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Text(
                    text = homework,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}