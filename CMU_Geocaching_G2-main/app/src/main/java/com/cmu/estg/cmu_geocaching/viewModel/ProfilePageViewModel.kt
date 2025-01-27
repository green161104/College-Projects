package com.cmu.estg.cmu_geocaching.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cmu.estg.cmu_geocaching.data.local.entities.User
import com.cmu.estg.cmu_geocaching.data.local.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfilePageViewModel(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null) // Error state
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        Log.d("inside profile view model", "im initing")
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            val firestoreUser = userRepository.fetchUserFromFirestore()

            if (firestoreUser != null) {
                userRepository.cacheUserLocally(firestoreUser)
                _currentUser.value = firestoreUser
            } else {
                val cachedUser = userRepository.getCachedUser()
                cachedUser?.let {
                   userRepository.cacheUserLocally(it)
                   _currentUser.value = it
              }
            }
        }
    }


    fun saveUser(user: User) = viewModelScope.launch {
        userRepository.saveUserToFirestore(user)
        userRepository.cacheUserLocally(user)
        _currentUser.value = user
    }


    fun updateUserProfile(
        name: String? = null,
        pronouns: String? = null,
    ) = viewModelScope.launch {
        val currentUserValue = currentUser.value ?: return@launch

        // Ensure that the name is not empty or null
        val updatedName = if (!name.isNullOrEmpty()) name else currentUserValue.name
        val updatedPronouns = pronouns ?: currentUserValue.pronouns

        val updatedUser = currentUserValue.copy(
            name = updatedName,
            pronouns = updatedPronouns,
        )

        try {
            userRepository.updateUserProfile(updatedUser)
            saveUser(updatedUser)
        } catch (e: Exception) {
            Log.e("ProfilePageViewModel", "Error updating user profile", e)
            _errorMessage.value = "Failed to update profile. Please try again."
        }
    }


    fun updateProfilePicture(picturePath: String) = viewModelScope.launch {
        val currentUserId = currentUser.value?.userId ?: return@launch

        try {
            val updatedUser = userRepository.updateProfilePicture(currentUserId, picturePath)
            saveUser(updatedUser!!)
        } catch (e: Exception) {
            Log.e("ProfilePageViewModel", "Error updating profile picture", e)
            _errorMessage.value = "Failed to update profile picture. Please try again."
        }
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }


}

class ProfilePageViewModelFactory(
    private val application: Application,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfilePageViewModel::class.java)) {
            Log.d("ViewModelFactory", "Creating ProfilePageViewModel")
            return ProfilePageViewModel(application, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}