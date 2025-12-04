package com.example.findesttest.data.repository

import com.example.findesttest.data.db.OrderDao
import com.example.findesttest.data.db.OrderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(private val orderDao: OrderDao): OrderRepository {
    override suspend fun saveOrder(items: String, totalPrice: Double, images: String) {
        val order = OrderEntity(
            items = items,
            totalPrice = totalPrice,
            images = images
        )
        orderDao.insertOrder(order)
    }


    override fun getAllOrders(): Flow<List<OrderEntity>> {
        return orderDao.getAllOrders()
    }

}