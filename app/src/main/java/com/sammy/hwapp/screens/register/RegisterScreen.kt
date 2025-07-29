package com.sammy.hwapp.screens.register

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.montanainc.simpleloginscreen.components.ClassSelectorComponent
import com.montanainc.simpleloginscreen.components.HeadingTextComponent
import com.montanainc.simpleloginscreen.components.MyTextFieldComponent
import com.montanainc.simpleloginscreen.components.NormalTextComponent
import com.montanainc.simpleloginscreen.components.PasswordTextFieldComponent
import com.montanainc.simpleloginscreen.components.RegisterButton
import com.sammy.hwapp.routeScreen
import com.sammy.hwapp.screens.login.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    view: RegisterViewModel = viewModel()
) {
    val uiState by view.uiState.collectAsState()
    val context = LocalContext.current
    val isLoading = uiState.isLoading
    val isLoaded = uiState.isLoaded
    var email by remember { mutableStateOf("") }
    var loginDn by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordDn by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("") }

    val error = uiState.onError
    val errorEmail = uiState.onErrorEmail
    val errorPassword = uiState.onErrorPassword
    val errorLoginDn = uiState.onErrorLoginDn
    val errorPasswordDn = uiState.onErrorPasswordDn
    val errorClass = uiState.onErrorClass
    if (isLoaded) routeScreen(navHostController, "Main")
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                NormalTextComponent(value = "Мир Ти,")
                HeadingTextComponent(value = "Создай Аккаунт")
                Spacer(modifier = Modifier.height(25.dp))

                Column {
                    MyTextFieldComponent(
                        labelValue = "Почта",
                        icon = Icons.Outlined.Email,
                        textValue = email,
                        onValueChange = {
                            email = it
                        },
                        isError = errorEmail != null
                    )
                    errorEmail?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    PasswordTextFieldComponent(
                        labelValue = "Пароль",
                        icon = Icons.Outlined.Lock,
                        textValue = password,
                        onValueChange = {
                            password = it
                        },
                        isError = errorPassword != null
                    )
                    errorPassword?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                    MyTextFieldComponent(
                        labelValue = "Логин дневника",
                        icon = Icons.Outlined.Person,
                        textValue = loginDn,
                        onValueChange = {
                            loginDn = it
                        },
                        isError = errorLoginDn != null
                    )
                    errorLoginDn?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    PasswordTextFieldComponent(
                        labelValue = "Пароль дневника",
                        icon = Icons.Outlined.Lock,
                        textValue = passwordDn,
                        onValueChange = {
                            passwordDn = it
                        },
                        isError = errorPasswordDn != null
                    )
                    ClassSelectorComponent(
                        selectedClass = selectedClass,
                        onClassSelected = { selectedClass = it },
                        icon = Icons.Outlined.School,
                        isError = errorClass != null
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    RegisterButton(
                        onClick = {
                            val validate = view.validate(email, password, loginDn, passwordDn, selectedClass)
                            if (!validate) return@RegisterButton
                            selectedClass =
                                selectedClass.replace("А", "A").replace("Б", "B").replace("В", "V")
                            view.checkDataDiaries(
                                context,
                                email = email,
                                password = password,
                                loginDnevnik = loginDn,
                                passwordDnevnik = passwordDn,
                                selectedClass = selectedClass,
                            )
                        },
                        isEnabled = true,
                        errorMessage = error,
                        navHostController
                    )
                }
            }
        }
    }
}