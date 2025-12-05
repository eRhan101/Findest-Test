package com.example.findesttest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class, CartEntity::class, OrderEntity::class, UserEntity::class], version = 6, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun userDao(): UserDao
}