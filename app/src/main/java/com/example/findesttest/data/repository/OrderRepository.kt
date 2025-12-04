package com.example.findesttest.data.repository

import androidx.lifecycle.LiveData
import com.example.findesttest.data.db.OrderEntity

interface OrderRepository {
    suspend fun saveOrder(items: String, totalPrice: Double, images: String)
    fun getAllOrders(): LiveData<List<OrderEntity>>
}