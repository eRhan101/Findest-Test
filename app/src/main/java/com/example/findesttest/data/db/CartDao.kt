package com.example.findesttest.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items WHERE userID = :userId" )
    fun getCartItems(userId: Int): LiveData<List<CartEntity>>

    @Query("SELECT * FROM cart_items WHERE id = :id LIMIT 1")
    suspend fun getCartItemByIdSync(id: Int): CartEntity?

    @Query("SELECT * FROM cart_items WHERE productId = :productId AND userId = :userId LIMIT 1")
    suspend fun getCartItemByIdAndUserId(productId: Int, userId: Int): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartEntity)

    @Delete
    suspend fun deleteCartItem(cartItem: CartEntity)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)
}