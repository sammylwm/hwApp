package com.sammy.hwapp.screens.main.fragments.grades

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GradesFragment(view: GradesViewModel = viewModel()) {
    val context = LocalContext.current
    val uiState by view.uiState.collectAsState()
    LaunchedEffect(Unit) {
        view.loadGrades(context)
    }
    if (uiState.isLoaded){
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            items(uiState.grades) { entry ->
                GradeCard(entry)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GradeCard(entry: GradeEntry) {
    val colorScheme = MaterialTheme.colorScheme

    val gradeColor = when (entry.grade.toFloatOrNull() ?: 0f) {
        in 0f..<3f -> colorScheme.error
        in 3f..<4f -> colorScheme.tertiary
        in 4f..5f -> colorScheme.primary
        else -> colorScheme.outline
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.subject,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = entry.date,
                        fontSize = 14.sp,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = entry.grade,
                    fontSize = 20.sp,
                    color = gradeColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
