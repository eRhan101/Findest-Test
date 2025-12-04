package com.example.findesttest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.model.ProductDto
import com.example.findesttest.data.repository.CartRepository
import com.example.findesttest.data.repository.ProductRepository
import com.example.findesttest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _productState = MutableStateFlow<UiState<List<ProductDto>>>(UiState.Loading)
    val productState: StateFlow<UiState<List<ProductDto>>> = _productState

    private val _addToCartState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val addToCartState: StateFlow<UiState<Boolean>> = _addToCartState

    private val _categoriesState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val categoriesState: StateFlow<UiState<List<String>>> = _categoriesState
    private var currentCategory: String? = null

    private val _selectedCategory = MutableStateFlow<String?>(null)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadProducts()
        loadCategories()
    }

    private fun loadProducts(category: String? = null) {
        viewModelScope.launch {
            _productState.value = UiState.Loading
            val products = if (category.isNullOrEmpty()) {
                productRepository.getProducts()
            } else {
                productRepository.getProductbyCategory(category)
            }
            products
                .catch { e ->
                    android.util.Log.e("HomeViewModel", "Error loading products", e)
                    _productState.value =
                        UiState.Error(e.message ?: "Failed to Load Products", throwable = e)
                }
                .collect {
                    if (it.isEmpty()) {
                        android.util.Log.e("HomeViewModel", "success but empty")
                        _productState.value = UiState.Success(emptyList())
                    } else {
                        android.util.Log.e("HomeViewModel", "success")
                        _productState.value = UiState.Success(it)
                    }
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = UiState.Loading
            val categories = productRepository.getCategories()
            categories
                .catch { e ->
                    _categoriesState.value =
                        UiState.Error(e.message ?: "Failed to Load Categories", throwable = e)
                }
                .collect {
                    _categoriesState.value = UiState.Success(it)
                }
        }
    }

    fun addToCart(product: ProductDto) {
        viewModelScope.launch {
            try {
                cartRepository.addToCart(product)
                _addToCartState.value = UiState.Success(true)
                _addToCartState.value = UiState.Loading
            } catch (e: Exception) {
                _addToCartState.value = UiState.Error(e.message ?: "Failed to add to cart", e)
            }
        }
    }


    fun onCategorySelected(category: String?) {
        if (currentCategory == category) {
            currentCategory = null
            _selectedCategory.value = null
            loadProducts(null)
        } else {
            currentCategory = category
            _selectedCategory.value = category
            loadProducts(category)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            loadProducts(currentCategory)
        } else {
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _productState.value = UiState.Loading

            productRepository.searchProducts(query)
                .catch { e ->
                    _productState.value = UiState.Error(e.message ?: "Search Failed", e)
                }
                .collect {
                    if (it.isEmpty()) {
                        _productState.value = UiState.Success(emptyList())
                    } else {
                        val dtos = it.map { entity ->
                            ProductDto(
                                id = entity.id,
                                title = entity.title,
                                price = entity.price,
                                description = entity.description,
                                category = entity.category,
                                image = entity.image,
                                rating = com.example.findesttest.data.model.RatingDto(
                                    rate = entity.ratingRate,
                                    count = entity.ratingCount
                                )
                            )
                        }

                        _productState.value = UiState.Success(dtos)
                    }
                }
        }
    }
}