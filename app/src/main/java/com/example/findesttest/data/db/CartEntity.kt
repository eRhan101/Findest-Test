package com.example.findesttest.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val userId: Int,
    val title: String,
    val price: Double,
    val image: String,
    val category: String,
    val quantity: Int = 1
)
