package com.example.findesttest.ui.detail

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
class DetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private var currentProductSource: LiveData<ProductDto>? = null

    private val _detailState = MediatorLiveData<UiState<ProductDto>>(UiState.Loading)
    val detailState: LiveData<UiState<ProductDto>> = _detailState

    private val _addToCartState = MediatorLiveData<UiState<Boolean>>(UiState.Loading)
    val addToCartState: LiveData<UiState<Boolean>> = _addToCartState


    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            currentProductSource?.let { _detailState.removeSource(it) }

            _detailState.value = UiState.Loading

            try {
                val source = productRepository.getProductsbyId(id)
                currentProductSource = source

                _detailState.addSource(source) {
                    _detailState.value = UiState.Success(it)
                }
            } catch (e: Exception) {
                _detailState.value =
                    UiState.Error(e.message ?: "Failed to Load Product", throwable = e)
            }


//            _detailState.value = UiState.Loading
//            val product = productRepository.getProductsbyId(id)
//            product
//                .catch { e ->
//                    _detailState.value =
//                        UiState.Error(e.message ?: "Failed to Load Product", throwable = e)
//                }
//                .collect {
//                    _detailState.value = UiState.Success(it)
//                }
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
}