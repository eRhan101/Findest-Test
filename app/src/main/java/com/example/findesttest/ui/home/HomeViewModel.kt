package com.example.findesttest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.model.ProductDto
import com.example.findesttest.data.repository.CartRepository
import com.example.findesttest.data.repository.ProductRepository
import com.example.findesttest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private var currentProductSource: LiveData<List<ProductDto>>? = null

    private val _productState = MediatorLiveData<UiState<List<ProductDto>>>(UiState.Loading)
    val productState: LiveData<UiState<List<ProductDto>>> = _productState

    private val _addToCartState = MediatorLiveData<UiState<Boolean>>(UiState.Loading)
    val addToCartState: LiveData<UiState<Boolean>> = _addToCartState

    private val _categoriesState = MediatorLiveData<UiState<List<String>>>(UiState.Loading)
    val categoriesState: LiveData<UiState<List<String>>> = _categoriesState
    private var currentCategory: String? = null

    private val _selectedCategory = MediatorLiveData<String?>(null)

    private val _searchQuery = MediatorLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    init {
        loadProducts()
        loadCategories()
    }

    private fun loadProducts(category: String? = null) {
        viewModelScope.launch {
            currentProductSource?.let { _productState.removeSource(it) }

            _productState.value = UiState.Loading

            try {
                val source: LiveData<List<ProductDto>> = if (category.isNullOrEmpty()) {
                    productRepository.getProducts()
                } else {
                    productRepository.getProductbyCategory(category)
                }

                currentProductSource = source

                _productState.addSource(source) {
                    if (it.isEmpty()) {
                        android.util.Log.e("HomeViewModel", "Success but empty")
                        _productState.value = UiState.Success(emptyList())
                    } else {
                        android.util.Log.e("HomeViewModel", "Success")
                        _productState.value = UiState.Success(it)
                    }
                }

            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Error loading products", e)
                _productState.value =
                    UiState.Error(e.message ?: "Failed to Load Products", throwable = e)
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = UiState.Loading
            try {
                val source = productRepository.getCategories()
                _categoriesState.addSource(source) {
                    _categoriesState.value = UiState.Success(it)
                }
            } catch (e: Exception) {
                _categoriesState.value =
                    UiState.Error(e.message ?: "Failed to Load Categories", throwable = e)
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
            currentProductSource?.let { _productState.removeSource(it) }

            _productState.value = UiState.Loading

            try {
                val source = productRepository.searchProducts(query)
                currentProductSource = source
                _productState.addSource(source) {
                    _productState.value = UiState.Success(it)
                }
            } catch (e: Exception) {
                _productState.value = UiState.Error(e.message ?: "Search Failed", e)
            }
        }
    }
}