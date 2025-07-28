package com.sammy.hwapp.screens.login

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sammy.hwapp.SharedPref
import com.sammy.hwapp.screens.splash.loadData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray

class LoginViewModel : ViewModel() {
    private val _onErrorEmail = MutableStateFlow<String?>(null)
    val onErrorEmail: StateFlow<String?> = _onErrorEmail

    private val _onErrorPassword = MutableStateFlow<String?>(null)
    val onErrorPassword: StateFlow<String?> = _onErrorPassword

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun validate(email: String, password: String): Boolean {
        var valid = true

        if (email.isBlank() || password.isBlank()) {
            _onErrorEmail.value = if (email.isBlank()) "Введите emaill" else null
            _onErrorPassword.value = if (password.isBlank()) "Введите пароль" else null
            valid = false
        } else if (email.length < 3 || password.length < 6) {
            _onErrorEmail.value = if (email.length < 3) "Логин слишком короткий" else null
            _onErrorPassword.value = if (password.length < 6) "Пароль слишком простой" else null
            valid = false
        }
        return valid
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun load(
        context: Context,
        email: String,
        password: String,
    ) {

        viewModelScope.launch {
            val result = LogIo.loginUser(email, password).toIntOrNull()
            when (result) {
                0 -> {
                    _onErrorEmail.value = "Пользователь не найден"
                    _onErrorPassword.value = null
                }
                1 -> {
                    _onErrorEmail.value = null
                    _onErrorPassword.value = "Неверный пароль"
                }
                2 -> {
                    _onErrorEmail.value = null
                    _onErrorPassword.value = null
                    _isLoading.value = true
                    val res = LogIo.getDatas(email)
                    val json = JSONArray(res)
                    val className = json.getString(0)
                    val loginDn = json.getString(1)
                    val passwordDn = json.getString(2)

                    val sharedPref = SharedPref(context, "UserData")
                    sharedPref.update(
                        mapOf(
                            "email" to email,
                            "password" to password,
                            "class" to className,
                            "loginDn" to loginDn,
                            "passwordDn" to passwordDn
                        )
                    )
                    loadData(context)
                    _isLoaded.value = true
                }
            }
        }

    }
}