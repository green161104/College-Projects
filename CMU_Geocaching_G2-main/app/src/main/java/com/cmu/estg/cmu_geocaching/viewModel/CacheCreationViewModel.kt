package com.cmu.estg.cmu_geocaching.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.data.local.entities.User
import com.cmu.estg.cmu_geocaching.data.local.repository.CacheRepository
import com.cmu.estg.cmu_geocaching.data.local.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CacheCreationViewModel(
    application: Application,
    private val cacheRepository: CacheRepository,
    private val userRepository: UserRepository
) :
    AndroidViewModel(application) {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    private val _cacheCreationSuccess = MutableStateFlow(false)
    val cacheCreationSuccess: StateFlow<Boolean> = _cacheCreationSuccess.asStateFlow()

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            val cachedUser = userRepository.getCachedUser()
            if (cachedUser != null) {
                _currentUser.value = cachedUser
            } else {
                val firestoreUser = userRepository.fetchUserFromFirestore()
                firestoreUser?.let {
                    userRepository.cacheUserLocally(it)
                    _currentUser.value = it
                }
            }
        }
    }

    fun createCache(
        cacheId: String,
        difficulty: String,
        latitude: String,
        longitude: String,
        description: String
    ) {
        viewModelScope.launch {
            val user = _currentUser.value
            if (user != null && user.points >= 150) {
                if (isValidLatitude(latitude) && isValidLongitude(longitude)) {
                    cacheRepository.upsertCache(
                        Cache(
                            cacheId,
                            createdBy = user.userId,
                            difficulty = difficulty,
                            latitude = latitude,
                            longitude = longitude,
                            rating = 5,
                            description = description
                        )
                    )
                    _errorMessage.value = null
                    updateUserPoints()
                    _cacheCreationSuccess.value = true // Indicate success
                }else{
                    _errorMessage.value ="Please enter valid latitude and longitude coordinates."
                    _cacheCreationSuccess.value = false
                }
            } else {
                _errorMessage.value = "You need at least 150 points to create a new cache."
                _cacheCreationSuccess.value = false
            }
        }
    }

    private fun updateUserPoints() = viewModelScope.launch {
        val currentUserValue = currentUser.value ?: return@launch

        val updatedUser = currentUserValue.copy(
          points = currentUserValue.points - 150
        )

        try {
            userRepository.updateUserPoints(updatedUser)
            saveUser(updatedUser)
        } catch (e: Exception) {
            Log.e("ProfilePageViewModel", "Error updating user points", e)
        }
    }

    private fun saveUser(user: User) = viewModelScope.launch {
        userRepository.saveUserToFirestore(user)
        userRepository.cacheUserLocally(user)
        _currentUser.value = user
    }

    fun isValidLatitude(latitude: String): Boolean {
        return try {
            val lat = latitude.toDouble()
            lat in -90.0..90.0
        } catch (e: Exception) {
            false
        }
    }

    fun isValidLongitude(longitude: String): Boolean {
        return try {
            val lon = longitude.toDouble()
            lon in -180.0..180.0
        } catch (e: Exception) {
            false
        }
    }
}
    
    class CacheCreationViewModelFactory(
        private val application: Application,
        private val cacheRepository: CacheRepository,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CacheCreationViewModel::class.java)) {
                Log.d("ViewModelFactory", "Creating CacheCreationViewModel")
                return CacheCreationViewModel(
                    application, cacheRepository,
                    userRepository = userRepository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
