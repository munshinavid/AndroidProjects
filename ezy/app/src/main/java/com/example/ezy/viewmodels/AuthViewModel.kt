package com.example.ezy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezy.*
import com.example.ezy.utils.PrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = EzyCommerceRepository()

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            repository.login(email, password).fold(
                onSuccess = { response ->
                    PrefsManager.saveUserData(
                        response.token,
                        response.user.id,
                        response.user.email,
                        response.user.username
                    )
                    _loginState.value = AuthState.Success(response)
                },
                onFailure = { _loginState.value = AuthState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            repository.register(username, email, password).fold(
                onSuccess = { response ->
                    PrefsManager.saveUserData(
                        response.token,
                        response.user.id,
                        response.user.email,
                        response.user.username
                    )
                    _registerState.value = AuthState.Success(response)
                },
                onFailure = { _registerState.value = AuthState.Error(it.message ?: "Registration failed") }
            )
        }
    }

    fun logout() {
        viewModelScope.launch { PrefsManager.clearUserData() }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val response: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}