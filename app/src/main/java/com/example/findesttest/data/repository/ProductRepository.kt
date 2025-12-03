package com.example.findesttest.data.repository

import com.example.findesttest.data.db.ProductEntity
import com.example.findesttest.data.model.ProductDto
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): Flow<List<ProductDto>>
    suspend fun getProductsbyId(id: Int): Flow<ProductDto>
    suspend fun getCategories(): Flow<List<String>>
    suspend fun getProductbyCategory(category: String): Flow<List<ProductDto>>
    suspend fun searchProducts(query: String): Flow<List<ProductEntity>>

}