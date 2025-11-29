package com.example.findesttest.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart_items WHERE id = :id")
    fun getCartItemById(id: Int): Flow<CartEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartEntity)

    @Delete
    suspend fun deleteCartItem(cartItem: CartEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}