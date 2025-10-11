package com.example.ezy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezy.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val repository = EzyCommerceRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadProducts()
        loadCategories()
    }

    fun loadProducts(category: String = "", search: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getProducts(category = category, search = search).fold(
                onSuccess = { _products.value = it.products },
                onFailure = { }
            )
            _isLoading.value = false
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().fold(
                onSuccess = { _categories.value = it.categories },
                onFailure = { }
            )
        }
    }

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            repository.getProduct(productId).fold(
                onSuccess = { _selectedProduct.value = it.product },
                onFailure = { }
            )
        }
    }
}