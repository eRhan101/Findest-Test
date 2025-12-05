package com.example.findesttest.di

import com.example.findesttest.data.repository.CartRepository
import com.example.findesttest.data.repository.CartRepositoryImpl
import com.example.findesttest.data.repository.OrderRepository
import com.example.findesttest.data.repository.OrderRepositoryImpl
import com.example.findesttest.data.repository.ProductRepository
import com.example.findesttest.data.repository.ProductRepositoryImpl
import com.example.findesttest.data.repository.UserRepository
import com.example.findesttest.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

}