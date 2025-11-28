package com.example.findesttest.di

import androidx.room.Room
import com.example.findesttest.data.db.AppDataBase
import com.example.findesttest.data.repository.ProductRepository
import com.example.findesttest.data.repository.ProductRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDataBase::class.java,
            "marketplace_db"
        ).fallbackToDestructiveMigration()
            .build()
    }
    single {
        get<AppDataBase>().productDao()
    }
    single<ProductRepository>{
        ProductRepositoryImpl(get(), get())
    }
}