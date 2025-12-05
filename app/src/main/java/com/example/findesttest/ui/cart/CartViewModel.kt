package com.example.findesttest.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.data.repository.CartRepository
import com.example.findesttest.utils.SessionManager
import com.example.findesttest.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _currentUserId = MutableLiveData<Int>()

    private val _cartItemsSource: LiveData<List<CartEntity>> = _currentUserId.switchMap { userId ->
        cartRepository.getCartItems()
    }

    val cartState: LiveData<UiState<List<CartEntity>>> = _cartItemsSource.map { items ->
        if (items.isEmpty()) {
            UiState.Success(emptyList())
        } else {
            UiState.Success(items)
        }
    }

    val totalPrice: LiveData<Double> = _cartItemsSource.map { items ->
        items.sumOf { it.price * it.quantity }
    }

    init {
        refreshCart()
    }

    fun refreshCart() {
        _currentUserId.value = sessionManager.getUserId()
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

    fun deleteItem(item: CartEntity) {
        viewModelScope.launch {
            cartRepository.removeFromCart(item)
        }
    }
}