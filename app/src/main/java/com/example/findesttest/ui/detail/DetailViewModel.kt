package com.example.findesttest.ui.detail

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
class DetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _detailState = MutableStateFlow<UiState<ProductDto>>(UiState.Loading)
    val detailState: StateFlow<UiState<ProductDto>> = _detailState

    private val _addToCartState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val addToCartState: StateFlow<UiState<Boolean>> = _addToCartState


    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            val product = productRepository.getProductsbyId(id)
            product
                .catch { e ->
                    _detailState.value =
                        UiState.Error(e.message ?: "Failed to Load Product", throwable = e)
                }
                .collect {
                    _detailState.value = UiState.Success(it)
                }
        }
    }

    fun addToCart(product: ProductDto){
        viewModelScope.launch {
            try {
                cartRepository.addToCart(product)
                _addToCartState.value = UiState.Success(true)
                _addToCartState.value = UiState.Loading
            } catch (e: Exception){
                _addToCartState.value = UiState.Error(e.message ?: "Failed to add to cart", e)
            }
        }
    }
}