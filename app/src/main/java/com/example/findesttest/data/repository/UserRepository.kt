package com.example.findesttest.data.repository

import androidx.lifecycle.LiveData
import com.example.findesttest.data.db.UserEntity

interface UserRepository {
    suspend fun register(user: UserEntity): Result<Boolean>
    suspend fun login(username: String, password: String): Result<UserEntity>
    fun logout()
    fun isLoggedIn(): Boolean
    fun getCurrentUser(): LiveData<UserEntity>
}