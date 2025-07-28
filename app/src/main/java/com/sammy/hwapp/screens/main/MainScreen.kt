package com.sammy.hwapp.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sammy.hwapp.screens.main.fragments.BottomNavigation
import com.sammy.hwapp.screens.main.fragments.NavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController)}
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavGraph(navHostController = navController)
        }

    }
}
