package com.example.findesttest.data.repository

import com.example.findesttest.data.db.OrderEntity
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun saveOrder(items: String, totalPrice: Double, images: String)
    fun getAllOrders(): Flow<List<OrderEntity>>
}