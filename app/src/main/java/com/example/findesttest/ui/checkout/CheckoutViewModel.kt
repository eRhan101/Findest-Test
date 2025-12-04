package com.example.findesttest.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.repository.CartRepository
import com.example.findesttest.data.repository.OrderRepository
import com.example.findesttest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    val cartItems = cartRepository.getCartItems()
        .catch { emit(emptyList()) }

    val totalPrice = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }

    private val _orderState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val orderState: StateFlow<UiState<Boolean>> = _orderState

    fun placeOrder() {
        viewModelScope.launch {
            try {
                val currentItems = cartItems.first()
                if (currentItems.isNotEmpty()) {
                    val total = currentItems.sumOf { it.price * it.quantity }
                    val summary = currentItems.joinToString(", ") { "${it.title} x${it.quantity}" }
                    val image = currentItems.first().image

                    orderRepository.saveOrder(summary, total, image)
                }
                cartRepository.clearCart()
                _orderState.value = UiState.Success(true)

            } catch (e: Exception) {
                _orderState.value = UiState.Error(e.message ?: "Failed to place order")
            }
        }
    }
}