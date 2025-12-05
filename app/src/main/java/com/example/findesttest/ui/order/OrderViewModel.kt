package com.example.findesttest.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.findesttest.data.db.OrderEntity
import com.example.findesttest.data.repository.OrderRepository
import com.example.findesttest.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    repository: OrderRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _currentuserId = MutableLiveData<Int>()

    val orders: LiveData<List<OrderEntity>> = _currentuserId.switchMap {
        repository.getAllOrders()
    }

    init {
        loadOrders()
    }

    fun loadOrders(){
        _currentuserId.value = sessionManager.getUserId()
    }
}