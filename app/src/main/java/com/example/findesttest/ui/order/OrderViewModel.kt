package com.example.findesttest.ui.order

import androidx.lifecycle.ViewModel
import com.example.findesttest.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(repository: OrderRepository) : ViewModel() {

    val orders = repository.getAllOrders()
}