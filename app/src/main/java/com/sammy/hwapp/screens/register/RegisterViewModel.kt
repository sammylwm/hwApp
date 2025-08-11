package com.sammy.hwapp.screens.register

import LogIo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.sammy.hwapp.SharedPref
import com.sammy.hwapp.screens.main.fragments.homework.UiState
import com.sammy.hwapp.screens.splash.loadData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val onError: String? = null,
    val onErrorEmail: String? = null,
    val onErrorPassword: String? = null,
    val onErrorLoginDn: String? = null,
    val onErrorPasswordDn: String? = null,
    val onErrorClass: String? = null,
    val showDialog: Boolean = false,
    val code: String = "",
    val codeReal: String = "",
    val codeIsError: Boolean = false,
    val isCodeConfirmed: Boolean = false
)


class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    fun validate(
        email: String,
        password: String,
        loginDn: String,
        passwordDn: String,
        selectedClass: String,
    ): Boolean {

        when {
            email.isBlank() || password.isBlank() || loginDn.isBlank()
                    || passwordDn.isBlank() || selectedClass.isBlank() -> {
                _uiState.value = _uiState.value.copy(
                    onErrorEmail = if (email.isBlank()) "Введите почту" else null,
                    onErrorLoginDn = if (loginDn.isBlank()) "Введите логин от дневника" else null,
                    onErrorPassword = if (password.isBlank()) "Введите пароль" else null,
                    onErrorPasswordDn = if (passwordDn.isBlank()) "Введите пароль от дневника" else null,
                    onErrorClass = if (selectedClass.isBlank()) "Выберите класс" else null,
                )
                return false
            }

            !email.matches(Regex("^[A-Za-z0-9+_uiState..-]+@[A-Za-z0-9.-]+$")) -> {
                _uiState.value = _uiState.value.copy(
                    onErrorEmail = "Введите корректный email"
                )
                return false
            }

            !password.matches(Regex("^[a-zA-Z0-9]+\$")) -> {
                _uiState.value = _uiState.value.copy(
                    onErrorPassword =
                        "Пароль должен содержать только латинские буквы и цифры"
                )
                return false
            }

            password.length < 6 -> {

                _uiState.value = _uiState.value.copy(onErrorPassword = "Пароль слишком короткий!")
                return false
            }
        }
        _uiState.value = _uiState.value.copy(
            onErrorEmail = null,
            onErrorPassword = null,
            onErrorLoginDn = null,
            onErrorPasswordDn = null,
            onErrorClass = null,
        )
        return true
    }

    fun showDialog(boolean: Boolean, email: String){
        if (boolean) {
            viewModelScope.launch {
                val res = LogIo.getCode(email).toInt()
                Log.d("SAMMY", res.toString())
                _uiState.update { it.copy(codeReal = res.toString()) }
            }
        }
        _uiState.update { it.copy(showDialog = boolean) }
    }
    fun updateCode(code: String) {
        _uiState.update { it.copy(code = code, codeIsError = false) }
    }
    fun checkCode(status: Boolean) {
        _uiState.update {
            if (status) {
                it.copy(isCodeConfirmed = true, codeIsError = false)
            } else {
                it.copy(codeIsError = true)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDataDiaries(
        context: android.content.Context,
        email: String,
        password: String,
        loginDnevnik: String,
        passwordDnevnik: String,
        selectedClass: String,
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            val status = LogIo.checkDiaries(loginDnevnik, passwordDnevnik).toInt()
            if (status == 0) {
                _uiState.value = _uiState.value.copy(
                    onErrorLoginDn = "Логин или пароль от дневника не верны",
                    onErrorPasswordDn = "Логин или пароль от дневника не верны",
                    isLoading = false
                )

                return@launch
            } else {
                val res = LogIo.registerUser(
                    email,
                    password,
                    selectedClass,
                    loginDnevnik,
                    passwordDnevnik
                ).toBoolean()
                if (!res) {
                    _uiState.value = _uiState.value.copy(
                        onError = "Произошла ошибка",
                        isLoading = false,
                    )
                } else {
                    val sharedPref = SharedPref(context, "UserData")
                    sharedPref.update(
                        mapOf(
                            "email" to email,
                            "password" to password,
                            "class" to selectedClass,
                            "loginDn" to loginDnevnik,
                            "passwordDn" to passwordDnevnik
                        )
                    )
                    loadData(context)
                    _uiState.value = _uiState.value.copy(isLoaded = true)
                }

            }
        }
    }


}