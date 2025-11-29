package com.example.findesttest.data.repository

import com.example.findesttest.data.db.CartDao
import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.data.model.ProductDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CartRepositoryImpl(private val cartDao: CartDao): CartRepository {
    override fun getCartItems(): Flow<List<CartEntity>> {
        return cartDao.getCartItems()
    }

    override suspend fun addToCart(product: ProductDto) {
        val existingItem = cartDao.getCartItemById(product.id).first()

        if (existingItem != null){
            val updatedItem = existingItem.copy(quantity = existingItem.quantity+1)
            cartDao.insertCartItem(updatedItem)
        } else {
            val newItem = CartEntity(
                id = product.id,
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
        if (newQuantity>0){
            val item = cartDao.getCartItemById(id).first()
            item?.let {
                cartDao.insertCartItem(it.copy(quantity = newQuantity))
            }
        }
    }

    override suspend fun removeFromCart(cartItem: CartEntity) {
        cartDao.deleteCartItem(cartItem)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}