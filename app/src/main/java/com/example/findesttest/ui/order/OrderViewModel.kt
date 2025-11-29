package com.example.findesttest.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn

class OrderViewModel(repository: OrderRepository) : ViewModel() {

    val orders = repository.getAllOrders()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}