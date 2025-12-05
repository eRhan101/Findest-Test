package com.example.findesttest.data.repository

import androidx.lifecycle.LiveData
import com.example.findesttest.data.db.UserDao
import com.example.findesttest.data.db.UserEntity
import com.example.findesttest.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
): UserRepository {
    override suspend fun register(user: UserEntity): Result<Boolean> {
        return try {
            val existing = userDao.getUserByUsername(user.username)
            if (existing != null) {
                Result.failure(Exception("Username already taken"))
            } else {
                userDao.registerUser(user)
                Result.success(true)
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun login(username: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.login(username, password)

            if (user != null) {
                sessionManager
                    .saveUserSession(user.id)
                Result.success(user)
            } else {
                Result.failure(Exception("Username/Password Incorrect"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override fun logout() {
        sessionManager.logout()
    }

    override fun isLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    override fun getCurrentUser(): LiveData<UserEntity> {
        val id = sessionManager.getUserId()
        return userDao.getUserById(id)
    }

}