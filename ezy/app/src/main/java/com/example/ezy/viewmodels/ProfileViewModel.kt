package com.example.ezy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezy.*
import com.example.ezy.utils.PrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = EzyCommerceRepository()

    private val _profile = MutableStateFlow<ProfileResponse?>(null)
    val profile: StateFlow<ProfileResponse?> = _profile

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    fun loadProfile() {
        viewModelScope.launch {
            val token = PrefsManager.token.first() ?: return@launch
            repository.getProfile(token).fold(
                onSuccess = { _profile.value = it },
                onFailure = { }
            )
        }
    }

    fun loadOrders() {
        viewModelScope.launch {
            val token = PrefsManager.token.first() ?: return@launch
            repository.getOrders(token).fold(
                onSuccess = { _orders.value = it.orders },
                onFailure = { }
            )
        }
    }
}