package com.example.findesttest.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.repository.CartRepository
import com.example.findesttest.data.repository.OrderRepository
import com.example.findesttest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    val cartItems = cartRepository.getCartItems()

    val totalPrice = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }

    private val _orderState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val orderState: LiveData<UiState<Boolean>> = _orderState

    fun placeOrder() {
        viewModelScope.launch {
            _orderState.value = UiState.Loading
            try {
                val currentItems = cartItems.value
                if (!currentItems.isNullOrEmpty()){
                    val total = currentItems.sumOf { it.price * it.quantity }
                    val summary = currentItems.joinToString(", ") { "${it.title} x${it.quantity}" }
                    val image = currentItems.firstOrNull()?.image ?: ""

                    orderRepository.saveOrder(summary, total, image)
                }
                cartRepository.clearCart()
                _orderState.value = UiState.Success(true)
            }catch (e: Exception){
                _orderState.value = UiState.Error(e.message ?: "Failed to place order")
            }
        }
    }
}