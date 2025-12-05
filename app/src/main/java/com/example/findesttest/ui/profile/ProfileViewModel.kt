package com.example.findesttest.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findesttest.data.db.UserEntity
import com.example.findesttest.data.repository.UserRepository
import com.example.findesttest.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {
    val userProfile: LiveData<UserEntity> = userRepository.getCurrentUser()

    fun logout(){
        sessionManager.logout()
    }
}