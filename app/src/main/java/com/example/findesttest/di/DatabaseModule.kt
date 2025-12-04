package com.example.findesttest.di

import android.content.Context
import androidx.room.Room
import com.example.findesttest.data.db.AppDataBase
import com.example.findesttest.data.db.CartDao
import com.example.findesttest.data.db.OrderDao
import com.example.findesttest.data.db.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context
    ): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "marketplace_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideProductDao(db: AppDataBase): ProductDao = db.productDao()

    @Provides
    fun provideOrderDao(db: AppDataBase): OrderDao = db.orderDao()

    @Provides
    fun provideCartDaoo(db: AppDataBase): CartDao = db.cartDao()
}