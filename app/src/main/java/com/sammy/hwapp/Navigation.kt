package com.sammy.hwapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sammy.hwapp.screens.login.LoginScreen
import com.sammy.hwapp.screens.main.MainScreen
import com.sammy.hwapp.screens.register.RegisterScreen
import com.sammy.hwapp.screens.splash.SplashScreen


@Composable
fun ActivityNavigation(navHostController: NavHostController) {
    val startDestination = "Splash"
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable("Splash") { SplashScreen(navHostController) }
        composable("Login") { LoginScreen(navHostController) }
        composable("Register") { RegisterScreen(navHostController) }
        composable("Main") { MainScreen() }
    }
}

fun routeScreen(navHostController: NavHostController, routeName: String) {
    navHostController.navigate(routeName) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}
