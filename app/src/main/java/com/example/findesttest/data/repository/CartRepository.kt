package com.example.findesttest.data.repository

import androidx.lifecycle.LiveData
import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.data.model.ProductDto

interface CartRepository {
    fun getCartItems(): LiveData<List<CartEntity>>
    suspend fun addToCart(product: ProductDto)
    suspend fun updateQuantity(id: Int, newQuantity: Int)
    suspend fun removeFromCart(cartItem: CartEntity)
    suspend fun clearCart()
}