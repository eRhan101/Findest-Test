package com.example.findesttest.data.repository

import androidx.lifecycle.LiveData
import com.example.findesttest.data.db.ProductEntity
import com.example.findesttest.data.model.ProductDto
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): LiveData<List<ProductDto>>
    suspend fun getProductsbyId(id: Int): LiveData<ProductDto>
    suspend fun getCategories(): LiveData<List<String>>
    suspend fun getProductbyCategory(category: String): LiveData<List<ProductDto>>
    suspend fun searchProducts(query: String): LiveData<List<ProductDto>>

}