package com.example.findesttest.data.repository

import com.example.findesttest.data.model.ProductDto

interface ProductRepository {
    suspend fun getProducts(): List<ProductDto>
    suspend fun getProductsbyId(id: Int): ProductDto
    suspend fun getCategories(): List<String>
    suspend fun getProductbyCategory(category: String): List<ProductDto>
}