package com.example.findesttest.data.repository

import com.example.findesttest.data.api.ProductApiService
import com.example.findesttest.data.model.ProductDto

class ProductRepositoryImpl(private val apiService: ProductApiService) : ProductRepository {
    override suspend fun getProducts(): List<ProductDto> {
        return apiService.getProducts()
    }

    override suspend fun getProductsbyId(id: Int): ProductDto {
        return apiService.getProductsbyId(id)
    }

    override suspend fun getCategories(): List<String> {
        return apiService.getCategories()
    }

    override suspend fun getProductbyCategory(category: String): List<ProductDto> {
        return apiService.getProductbyCategory(category)
    }

}