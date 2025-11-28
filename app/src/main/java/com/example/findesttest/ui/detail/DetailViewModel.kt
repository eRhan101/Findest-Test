package com.example.findesttest.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.model.ProductDto
import com.example.findesttest.data.repository.ProductRepository
import com.example.findesttest.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _detailState = MutableStateFlow<UiState<ProductDto>>(UiState.Loading)
    val detailState: StateFlow<UiState<ProductDto>> = _detailState

    fun loadProductDetail(id: Int){
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            try {
                val product = productRepository.getProductsbyId(id)
                _detailState.value = UiState.Success(product)
            } catch (e: Exception) {
                _detailState.value = UiState.Error(e.message ?: "Failed to Load Product", throwable = e)
            }
        }
    }
}