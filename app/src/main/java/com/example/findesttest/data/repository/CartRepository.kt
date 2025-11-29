package com.example.findesttest.data.repository

import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.data.model.ProductDto
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartEntity>>
    suspend fun addToCart(product: ProductDto)
    suspend fun updateQuantity(id: Int, newQuantity: Int)
    suspend fun removeFromCart(cartItem: CartEntity)
    suspend fun clearCart()
}