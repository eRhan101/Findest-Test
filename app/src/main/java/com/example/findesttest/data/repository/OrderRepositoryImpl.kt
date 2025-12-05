package com.example.findesttest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.findesttest.data.db.OrderDao
import com.example.findesttest.data.db.OrderEntity
import com.example.findesttest.utils.SessionManager
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val sessionManager: SessionManager
) : OrderRepository {
    override suspend fun saveOrder(items: String, totalPrice: Double, images: String) {
        val userId = sessionManager.getUserId()
        if (userId != -1) {
            val order = OrderEntity(
                userId = userId,
                items = items,
                totalPrice = totalPrice,
                images = images
            )
            orderDao.insertOrder(order)
        }
    }


    override fun getAllOrders(): LiveData<List<OrderEntity>> {
        val userId = sessionManager.getUserId()

        if (userId == -1) {
            return MutableLiveData(emptyList())
        }
        return orderDao.getAllOrders(userId)
    }

}