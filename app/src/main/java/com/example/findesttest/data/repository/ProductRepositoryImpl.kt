package com.example.findesttest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.findesttest.data.api.ProductApiService
import com.example.findesttest.data.db.ProductDao
import com.example.findesttest.data.db.ProductEntity
import com.example.findesttest.data.model.ProductDto
import com.example.findesttest.data.model.RatingDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService,
    private val productDao: ProductDao
) : ProductRepository {

    override suspend fun getProducts(): LiveData<List<ProductDto>> {
            withContext (Dispatchers.IO) {
                saveApiDataToDb()
            }


            return productDao.getAllProducts().map {
                entities -> entities.map { it.toDto() }
            }

    }

    override suspend fun getProductsbyId(id: Int): LiveData<ProductDto> {
        return productDao.getProductById(id)
            .map { entity -> entity?.toDto()!! }
    }

    override suspend fun getCategories(): LiveData<List<String>> {
        return productDao.getUniqueCategories()
    }

    override suspend fun getProductbyCategory(category: String): LiveData<List<ProductDto>> {
        return productDao.getProductsByCategory(category)
            .map { entities -> entities.map { it.toDto() } }
    }

    override suspend fun searchProducts(query: String): LiveData<List<ProductDto>> {
        return productDao.searchProducts(query).map { entities -> entities.map { it.toDto() } }
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