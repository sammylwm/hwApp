package com.sammy.hwapp.screens.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.montanainc.simpleloginscreen.components.HeadingTextComponent
import com.montanainc.simpleloginscreen.components.MyTextFieldComponent
import com.montanainc.simpleloginscreen.components.NormalTextComponent
import com.montanainc.simpleloginscreen.components.PasswordTextFieldComponent
import com.montanainc.simpleloginscreen.ui.theme.AccentColor
import com.montanainc.simpleloginscreen.ui.theme.Secondary
import com.montanainc.simpleloginscreen.ui.theme.TextColor
import com.sammy.hwapp.routeScreen





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(navHostController: NavHostController, view: LoginViewModel = viewModel()) {
    val isLoaded by view.isLoaded.collectAsState()
    val isLoading by view.isLoading.collectAsState()

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val emailError by view.onErrorEmail.collectAsState()
    val passwordError by view.onErrorPassword.collectAsState()

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
                Column {
                    NormalTextComponent(value = "Здравствуй, ")
                    HeadingTextComponent(value = "С Возвращением")
                }

                Spacer(modifier = Modifier.height(25.dp))

                Column {
                    MyTextFieldComponent(
                        labelValue = "Почта",
                        icon = Icons.Outlined.Email,
                        textValue = email,
                        onValueChange = {
                            email = it
                        },
                        isError = emailError != null
                    )
                    emailError?.let {
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
                        isError = passwordError != null
                    )
                    passwordError?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            Secondary,
                                            AccentColor
                                        )
                                    ),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .fillMaxWidth(0.8f)
                                .clickable {
                                    val isValid = view.validate(email, password)
                                    if (isValid) {
                                        view.load(context, email, password)
                                    }
                                }
                                .heightIn(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Войти", color = Color.White, fontSize = 20.sp)
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Row {
                            Text(
                                text = "Нет аккаунта? ",
                                style = TextStyle(color = TextColor, fontSize = 15.sp)
                            )
                            Text(
                                text = "Зарегистрироваться",
                                style = TextStyle(color = Secondary, fontSize = 15.sp),
                                modifier = Modifier.clickable {
                                    routeScreen(navHostController, "Register")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
