package com.example.findesttest.di

import com.example.findesttest.data.api.ApiConstants
import com.example.findesttest.data.api.ProductApiService
import com.example.findesttest.data.repository.ProductRepository
import com.example.findesttest.data.repository.ProductRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<ProductApiService> {
        get<Retrofit>().create(ProductApiService::class.java)
    }
    single<ProductRepository> {
        ProductRepositoryImpl(get())
    }
}