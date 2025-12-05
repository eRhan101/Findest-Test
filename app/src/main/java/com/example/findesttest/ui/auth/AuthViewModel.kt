package com.example.findesttest.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.db.UserEntity
import com.example.findesttest.data.repository.UserRepository
import com.example.findesttest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<Boolean>>()
    val loginState: LiveData<UiState<Boolean>> = _loginState

    private val _registerState = MutableLiveData<UiState<Boolean>>()
    val registerState: LiveData<UiState<Boolean>> = _registerState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = UiState.Error("Username or password is empty")
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val result = userRepository.login(username, password)
            if (result.isSuccess) {
                _loginState.value = UiState.Success(true)
            } else {
                _loginState.value =
                    UiState.Error(result.exceptionOrNull()?.message ?: "Login Failed")
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = UiState.Error("Username, email or password is empty")
            return
        }

        viewModelScope.launch {
            _registerState.value = UiState.Loading
            val result = userRepository.register(
                UserEntity(
                    username = username,
                    email = email,
                    password = password
                )
            )
            if (result.isSuccess){
                _registerState.value = UiState.Success(true)
            } else {
                _registerState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Registration Failed")
            }

        }

    }

    fun resetLoginState() {
        _loginState.value = UiState.Idle
    }

    fun resetRegisterState(){
        _registerState.value = UiState.Idle
    }
}