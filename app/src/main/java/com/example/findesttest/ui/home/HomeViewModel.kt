package com.example.findesttest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.model.ProductDto
import com.example.findesttest.data.repository.ProductRepository
import com.example.findesttest.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _productState = MutableStateFlow<UiState<List<ProductDto>>>(UiState.Loading)
    val productState: StateFlow<UiState<List<ProductDto>>> = _productState

    private val _categoriesState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val categoriesState: StateFlow<UiState<List<String>>> = _categoriesState

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadProducts()
        loadCategories()
    }

    fun loadProducts(category: String? = null) {
        viewModelScope.launch {
            _productState.value = UiState.Loading
            try {
                val products = if (category.isNullOrEmpty()) {
                    productRepository.getProducts()
                } else {
                    productRepository.getProductbyCategory(category)
                }
                _productState.value = UiState.Success(products)
            } catch (e: Exception) {
                _productState.value =
                    UiState.Error(e.message ?: "Failed to Load Products", throwable = e)

            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = UiState.Loading
            try {
                val categories = productRepository.getCategories()
                _categoriesState.value = UiState.Success(categories)
            } catch (e: Exception) {
                _categoriesState.value =
                    UiState.Error(e.message ?: "Failed to Load Categories", throwable = e)
            }
        }
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
        loadProducts(category)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        // TO DO
    }
}