package com.example.findesttest.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Int): ProductEntity?

    @Query("DELETE FROM products")
    suspend fun clearAll()

    @Query ("SELECT DISTINCT category FROM products ORDER BY category ASC")
    suspend fun getUniqueCategories(): List<String>
}