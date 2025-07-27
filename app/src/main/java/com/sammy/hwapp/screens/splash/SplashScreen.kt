package com.sammy.hwapp.screens.splash

import LogIo
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.sammy.hwapp.SharedPref
import com.sammy.hwapp.routeScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _tasks = MutableSharedFlow<String>()
    private val _loginResult = MutableStateFlow<Int?>(null)
    val loginResult: StateFlow<Int?> = _loginResult

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    @RequiresApi(Build.VERSION_CODES.O)
    fun load(context: Context) {
        val sharedPref = SharedPref(context = context, "UserData")
        if (sharedPref.contains("email")) {
            val email = sharedPref.get("email")
            val password = sharedPref.get("password")
            viewModelScope.launch {
                val result = LogIo.loginUser(email, password).toIntOrNull()
                _loginResult.value = result
                loadData(context)
                _isLoaded.value = true
            }
        } else {
            _loginResult.value = 0
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreen(
    navHostController: NavHostController,
    view: SplashViewModel = viewModel()
) {
    val isLoaded by view.isLoaded.collectAsState()
    val context = LocalContext.current
    val loginResult by view.loginResult.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (!isLoaded) CircularProgressIndicator()
    }

    LaunchedEffect(Unit) {
        view.load(context)
    }
    if (isLoaded){
        val routeName = if (loginResult == 2) "Main" else "Login"
        routeScreen(navHostController, routeName)
    }

}
