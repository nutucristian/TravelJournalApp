package com.example.traveljournal.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveljournal.R

class LoginViewModel : ViewModel() {

    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // Handle login logic
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginFormState.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotEmpty()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
