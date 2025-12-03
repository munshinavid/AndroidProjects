package com.example.ecommapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommapp.EcommApp
import com.example.ecommapp.data.local.entities.Product
import com.example.ecommapp.data.repository.EcommRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EcommRepository((application as EcommApp).database)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _priceRange = MutableStateFlow(0f..50000f)
    val priceRange: StateFlow<ClosedFloatingPointRange<Float>> = _priceRange.asStateFlow()

    val categories: StateFlow<List<String>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val products: StateFlow<List<Product>> = combine(
        repository.getAllProducts(),
        _searchQuery,
        _selectedCategory,
        _priceRange
    ) { products, query, category, range ->
        products.filter { product ->
            val matchesSearch = query.isEmpty() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)

            val matchesCategory = category == null || product.category == category

            val matchesPrice = product.price >= range.start && product.price <= range.endInclusive

            matchesSearch && matchesCategory && matchesPrice
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
        _priceRange.value = range
    }

    fun getProductById(productId: Int): StateFlow<Product?> {
        return repository.getProductById(productId)
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        _priceRange.value = 0f..50000f
    }
}