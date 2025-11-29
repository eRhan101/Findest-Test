package com.example.findesttest.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val items: String,
    val totalPrice: Double,
    val images: String

)
