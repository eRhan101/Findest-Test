package com.example.findesttest.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.data.repository.CartRepository
import com.example.findesttest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    private var currentProductSource: LiveData<List<CartEntity>>? = null

    private val _cartState = MediatorLiveData<UiState<List<CartEntity>>>(UiState.Loading)
    val cartState: LiveData<UiState<List<CartEntity>>> = _cartState

    private val _totalPrice = MediatorLiveData(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            currentProductSource?.let { _cartState.removeSource(it) }

            _cartState.value = UiState.Loading

            try {
                val source = cartRepository.getCartItems()
                currentProductSource = source
                _cartState.addSource(source){
                    _cartState.value = UiState.Success(it)
                    calculateTotal(it)
                }
            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.message ?: "Error loading cart", e)
            }
        }
    }

    private fun calculateTotal(items: List<CartEntity>) {
        var total = 0.0
        for (item in items) {
            total += item.price * item.quantity
        }
        _totalPrice.value = total
    }

    fun increaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            cartRepository.updateQuantity(item.id, item.quantity + 1)
        }
    }

    fun decreaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                cartRepository.updateQuantity(item.id, item.quantity - 1)
            } else {
                cartRepository.removeFromCart(item)
            }
        }
    }

    fun deleteItem(item: CartEntity){
        viewModelScope.launch {
            cartRepository.removeFromCart(item)
        }
    }
}