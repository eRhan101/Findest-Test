package com.example.findesttest.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): LiveData<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): LiveData<ProductEntity?>

    @Query("DELETE FROM products")
    suspend fun clearAll()

    @Query("SELECT DISTINCT category FROM products ORDER BY category ASC")
    fun getUniqueCategories(): LiveData<List<String>>

    @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%'")
    fun searchProducts(query: String): LiveData<List<ProductEntity>>
}