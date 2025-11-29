package com.example.findesttest.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val category: String,
    val quantity: Int = 1
)
