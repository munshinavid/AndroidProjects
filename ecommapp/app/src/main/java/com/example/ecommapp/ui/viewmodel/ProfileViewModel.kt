package com.example.ecommapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommapp.EcommApp
import com.example.ecommapp.data.local.entities.Address
import com.example.ecommapp.data.repository.EcommRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EcommRepository((application as EcommApp).database)

    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    fun loadAddresses(userId: Int) {
        viewModelScope.launch {
            repository.getUserAddresses(userId).collect { addresses ->
                _addresses.value = addresses
            }
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            if (address.isDefault) {
                repository.clearDefaultAddress(address.userId)
            }
            repository.addAddress(address)
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            if (address.isDefault) {
                repository.clearDefaultAddress(address.userId)
            }
            repository.updateAddress(address)
        }
    }

    fun deleteAddress(address: Address) {
        viewModelScope.launch {
            repository.deleteAddress(address)
        }
    }

    fun getDefaultAddress(): Address? {
        return _addresses.value.firstOrNull { it.isDefault }
    }
}