package com.example.findesttest.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.data.repository.CartRepository
import com.example.findesttest.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    private val _cartState = MutableStateFlow<UiState<List<CartEntity>>>(UiState.Loading)
    val cartState: StateFlow<UiState<List<CartEntity>>> = _cartState

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems()
                .catch { e ->
                    _cartState.value = UiState.Error(e.message ?: "Error loadingg cart", e)
                }
                .collect {
                    _cartState.value = UiState.Success(it)
                    calculateTotal(it)
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