package com.example.findesttest.data.api

import com.example.findesttest.data.model.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
    @GET("products/{id}")
    suspend fun getProductsbyId(
        @Path("id") id: Int
    ): ProductDto
    @GET("products/categories")
    suspend fun getCategories(): List<String>
    @GET("products/category/{category}")
    suspend fun  getProductbyCategory(
        @Path("category") category: String
    ): List<ProductDto>
}
