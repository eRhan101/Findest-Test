package com.example.findesttest.data.repository

import androidx.lifecycle.LiveData
import com.example.findesttest.data.db.CartDao
import com.example.findesttest.data.db.CartEntity
import com.example.findesttest.data.model.ProductDto
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartDao: CartDao): CartRepository {
    override fun getCartItems(): LiveData<List<CartEntity>> {
        return cartDao.getCartItems()
    }

    override suspend fun addToCart(product: ProductDto) {

        val existingItem = cartDao.getCartItemByIdSync(product.id)

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
        cartDao.clearCart()
    }
}