package com.example.findesttest.data.repository

import com.example.findesttest.data.api.ProductApiService
import com.example.findesttest.data.db.ProductDao
import com.example.findesttest.data.db.ProductEntity
import com.example.findesttest.data.model.ProductDto
import com.example.findesttest.data.model.RatingDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val apiService: ProductApiService,
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun getProducts(): List<ProductDto> {
        return withContext(Dispatchers.IO) {
            try {
                val apiProducts = apiService.getProducts()
                val entities = apiProducts.map { it.toEntity() }
                productDao.insertAll(entities)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val dbProducts = productDao.getAllProducts()
            dbProducts.map { it.toDto() }
        }
    }

    override suspend fun getProductsbyId(id: Int): ProductDto {
        return withContext(Dispatchers.IO) {
            val dbProduct = productDao.getProductById(id)

            if (dbProduct != null) {
                return@withContext dbProduct.toDto()
            }

            val apiProduct = apiService.getProductsbyId(id)
            apiProduct
        }

    }

    override suspend fun getCategories(): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getCategories()
            } catch (e: Exception) {
                productDao.getUniqueCategories()
            }
        }

    }

    override suspend fun getProductbyCategory(category: String): List<ProductDto> {
        return withContext(Dispatchers.IO) {
            val localProducts = productDao.getProductsByCategory(category)

            if (localProducts.isNotEmpty()) {
                return@withContext localProducts.map { it.toDto() }
            }

            try {
                val apiProducts = apiService.getProductbyCategory(category)
                val entities = apiProducts.map { it.toEntity() }
                productDao.insertAll(entities)

                return@withContext apiProducts
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    private fun ProductDto.toEntity(): ProductEntity {
        return ProductEntity(
            id = this.id,
            title = this.title,
            price = this.price,
            description = this.description,
            category = this.category,
            image = this.image,
            ratingRate = this.rating?.rate ?: 0.0,
            ratingCount = this.rating?.count ?: 0
        )
    }

    private fun ProductEntity.toDto(): ProductDto {
        return ProductDto(
            id = this.id,
            title = this.title,
            price = this.price,
            description = this.description,
            category = this.category,
            image = this.image,
            rating = RatingDto(this.ratingRate, this.ratingCount)
        )
    }


}