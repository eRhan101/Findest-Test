package com.example.findesttest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.findesttest.data.db.CartDao
import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.data.model.ProductDto
import com.example.findesttest.utils.SessionManager
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val sessionManager: SessionManager
) : CartRepository {
    override fun getCartItems(): LiveData<List<CartEntity>> {
        val userId = sessionManager.getUserId()
        if (userId == -1) {
            return MutableLiveData(emptyList())
        }
        return cartDao.getCartItems(userId)
    }

    override suspend fun addToCart(product: ProductDto) {
        val userId = sessionManager.getUserId()
        if (userId == -1) {
            return
        }

        val existingItem = cartDao.getCartItemByIdAndUserId(product.id, userId)

        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            cartDao.insertCartItem(updatedItem)
        } else {
            val newItem = CartEntity(
                productId = product.id,
                userId = userId,
                title = product.title,
                price = product.price,
                image = product.image,
                category = product.category,
                quantity = 1
            )
            cartDao.insertCartItem(newItem)
        }
    }

    override suspend fun updateQuantity(id: Int, newQuantity: Int) {
        if (newQuantity > 0) {
            val item = cartDao.getCartItemByIdSync(id)
            item?.let {
                cartDao.insertCartItem(it.copy(quantity = newQuantity))
            }
        }
    }

    override suspend fun removeFromCart(cartItem: CartEntity) {
        cartDao.deleteCartItem(cartItem)
    }

    override suspend fun clearCart() {
        val userId = sessionManager.getUserId()
        if (userId != -1) {
            cartDao.clearCart(userId)
        }
    }
}