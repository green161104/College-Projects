package com.cmu.estg.cmu_geocaching.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class HomePageViewModel(
    application: Application,
    private val userRepository: UserRepository,
    private val cacheRepository: CacheRepository
) : AndroidViewModel(application) {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    private val _allCaches = MutableLiveData<List<Cache>>()
    val allCaches: LiveData<List<Cache>> get() = _allCaches
    private val _hasPromptBeenShown = MutableStateFlow(false)
    val hasPromptBeenShown: StateFlow<Boolean> = _hasPromptBeenShown



    init {
        fetchCurrentUser()
        getAllCachesFirestore()
    }

    fun setPromptShown() {
        _hasPromptBeenShown.value = true
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


    private fun getAllCachesFirestore() {
        // Observe the LiveData from the repository
        cacheRepository.getAllCachesFromFirestore().observeForever { caches ->
            _allCaches.value = caches ?: emptyList() // Update the internal LiveData
        }
    }


}

class HomePageViewModelFactory(
    private val application: Application,
    private val userRepository: UserRepository,
    private val cacheRepository: CacheRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomePageViewModel::class.java)) {
            Log.d("ViewModelFactory", "Creating UserViewModel")
            return HomePageViewModel(application, userRepository, cacheRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}