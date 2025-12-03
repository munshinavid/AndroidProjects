package com.example.ecommapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommapp.EcommApp
import com.example.ecommapp.data.local.entities.User
import com.example.ecommapp.data.repository.EcommRepository
import com.example.ecommapp.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EcommRepository((application as EcommApp).database)
    private val preferencesManager = PreferencesManager(application)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            preferencesManager.userId.collect { userId ->
                if (userId != null) {
                    repository.getUserById(userId).collect { user ->
                        if (user != null) {
                            _currentUser.value = user
                            _authState.value = AuthState.Authenticated(user.id)
                        } else {
                            _authState.value = AuthState.Unauthenticated
                        }
                    }
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = repository.login(email, password)
                if (user != null) {
                    preferencesManager.saveUserId(user.id)
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated(user.id)
                } else {
                    _authState.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesManager.clearUserId()
            _currentUser.value = null
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun updateUserProfile(name: String, phone: String) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                val updatedUser = user.copy(name = name, phone = phone)
                repository.updateUser(updatedUser)
                _currentUser.value = updatedUser
            }
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val userId: Int) : AuthState()
    data class Error(val message: String) : AuthState()
}