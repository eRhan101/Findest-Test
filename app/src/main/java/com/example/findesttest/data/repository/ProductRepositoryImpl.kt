package com.example.findesttest.data.repository

import com.example.findesttest.data.api.ProductApiService
import com.example.findesttest.data.db.ProductDao
import com.example.findesttest.data.db.ProductEntity
import com.example.findesttest.data.model.ProductDto
import com.example.findesttest.data.model.RatingDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService,
    private val productDao: ProductDao
) : ProductRepository {

    override suspend fun getProducts(): Flow<List<ProductDto>> {
        return channelFlow {
            launch (Dispatchers.IO) {
                saveApiDataToDb()
            }

            productDao.getAllProducts()
                .map { entities -> entities.map {
                    it.toDto()
                }}.collectLatest { send(it) }
        }
    }

    override suspend fun getProductsbyId(id: Int): Flow<ProductDto> {
        return productDao.getProductById(id)
            .map { entity -> entity?.toDto()!! }
    }

    override suspend fun getCategories(): Flow<List<String>> {
        return productDao.getUniqueCategories()
    }

    override suspend fun getProductbyCategory(category: String): Flow<List<ProductDto>> {
        return productDao.getProductsByCategory(category)
            .map { entities -> entities.map { it.toDto() } }
    }

    override suspend fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return productDao.searchProducts(query)
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

    private suspend fun saveApiDataToDb() {
        try {
            val apiProducts = apiService.getProducts()
            val entities = apiProducts.map { it.toEntity() }
            productDao.insertAll(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}