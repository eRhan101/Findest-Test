package com.example.findesttest.utils

import org.koin.core.logger.MESSAGE

sealed class UiState<out T>  {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : UiState<Nothing>()
}