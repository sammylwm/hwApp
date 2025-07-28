package com.sammy.hwapp.screens.main.fragments

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sammy.hwapp.screens.main.fragments.allGrades.GradesAllFragment
import com.sammy.hwapp.screens.main.fragments.grades.GradesFragment
import com.sammy.hwapp.screens.main.fragments.homework.HwFragment

sealed class BottomItemMy (
    val title: String,
    val iconId: ImageVector,
    val route: String
) {
    data object HwScreen: BottomItemMy("Дз", Icons.Default.Book, "hw_screen")
    data object Grades: BottomItemMy("Оценки", Icons.Default.Grade, "grades_screen")
    data object AllGrades: BottomItemMy("Всё", Icons.Default.Grade, "grades_all_screen")
}

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "hw_screen") {
        composable("hw_screen") { HwFragment() }
        composable("grades_screen") { GradesFragment() }
        composable("grades_all_screen") { GradesAllFragment() }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val listItems = listOf(BottomItemMy.HwScreen, BottomItemMy.Grades, BottomItemMy.AllGrades)
    NavigationBar(){
        var currentRoute by remember { mutableStateOf("hw_screen") }
        listItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    currentRoute = item.route
                    navController.navigate(item.route)
                          },
                icon = { Icon(imageVector = item.iconId, contentDescription = item.title) },
                label = { Text(text = item.title) }
            )

        }
    }
}