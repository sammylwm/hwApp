package com.sammy.hwapp.screens.register

import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sammy.hwapp.routeScreen

@Composable
fun RegisterScreen(navHostController: NavHostController) {
    ElevatedButton(
        onClick = { routeScreen(navHostController, "Main") }
    ) {
        Text("Main")
    }
}