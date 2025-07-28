package com.sammy.hwapp.screens.register

import LogIo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.sammy.hwapp.SharedPref
import com.sammy.hwapp.screens.splash.loadData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _onError = MutableStateFlow<String?>(null)
    val onError: StateFlow<String?> = _onError

    private val _onErrorEmail = MutableStateFlow<String?>(null)
    val onErrorEmail: StateFlow<String?> = _onErrorEmail

    private val _onErrorPassword = MutableStateFlow<String?>(null)
    val onErrorPassword: StateFlow<String?> = _onErrorPassword

    private val _onErrorLoginDn = MutableStateFlow<String?>(null)
    val onErrorLoginDn: StateFlow<String?> = _onErrorLoginDn

    private val _onErrorPasswordDn = MutableStateFlow<String?>(null)
    val onErrorPasswordDn: StateFlow<String?> = _onErrorPasswordDn

    private val _onErrorClass = MutableStateFlow<String?>(null)
    val onErrorClass: StateFlow<String?> = _onErrorClass



    fun validate(email: String, password: String, loginDn: String, passwordDn: String, selectedClass: String): Boolean{
        when {
            email.isBlank() || password.isBlank() || loginDn.isBlank()
                    || passwordDn.isBlank() || selectedClass.isBlank() -> {
                _onErrorEmail.value = if (email.isBlank()) "Введите почту" else null
                _onErrorLoginDn.value =
                    if (loginDn.isBlank()) "Введите логин от дневника" else null
                _onErrorPassword.value =
                    if (password.isBlank()) "Введите пароль" else null
                _onErrorPasswordDn.value =
                    if (passwordDn.isBlank()) "Введите пароль от дневника" else null
                _onErrorClass.value =
                    if (selectedClass.isBlank()) "Выберите класс" else null
                return false
            }

            !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) -> {
                _onErrorEmail.value = "Введите корректный email"
                return false
            }

            !password.matches(Regex("^[a-zA-Z0-9]+\$")) -> {
                _onErrorPassword.value =
                    "Пароль должен содержать только латинские буквы и цифры"
                return false
            }

            password.length < 6 -> {
                _onErrorPassword.value = "Пароль слишком короткий!"
                return false
            }
        }
        _onErrorEmail.value = null
        _onErrorPassword.value = null
        _onErrorLoginDn.value = null
        _onErrorPasswordDn.value = null
        _onErrorClass.value = null
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDataDiaries(
        context: android.content.Context,
        email: String,
        password: String,
        loginDnevnik: String,
        passwordDnevnik: String,
        selectedClass: String,
    ){
        viewModelScope.launch {
            _isLoading.value = true
            val status = LogIo.checkDiaries(loginDnevnik, passwordDnevnik).toInt()
            if (status == 0) {
                _onErrorLoginDn.value = "Логин или пароль от дневника не верны"
                _onErrorPasswordDn.value = "Логин или пароль от дневника не верны"
                _isLoading.value = false
                return@launch
            } else {
                val res = LogIo.registerUser(email, password, selectedClass, loginDnevnik, passwordDnevnik).toBoolean()
                if (!res) {
                    _onError.value = "Произошла ошибка"
                    _isLoading.value = false
                } else {
                    val sharedPref = SharedPref(context, "UserData")
                    sharedPref.update(mapOf("email" to email, "password" to password,
                        "class" to selectedClass, "loginDn" to loginDnevnik, "passwordDn" to passwordDnevnik))
                    loadData(context)
                    _isLoaded.value = true
                }

            }
        }
    }



}