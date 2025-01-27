package com.cmu.estg.cmu_geocaching.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.UserCacheCrossRef
import com.cmu.estg.cmu_geocaching.data.local.repository.UserCacheCrossRefRepository
import com.cmu.estg.cmu_geocaching.data.local.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CacheHistoryViewModel(
    application: Application,
    private val userCacheRepository: UserCacheCrossRefRepository,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    private val _userHistory = MutableLiveData<List<UserCacheCrossRef>>()
    val userHistory: LiveData<List<UserCacheCrossRef>> get() = _userHistory

    init {
        getUserHistory()
    }

    private fun getUserHistory() {
        val currentUserId = userRepository.getCurrentUserId()
        Log.d("CacheHistoryViewModel", "Current User ID: $currentUserId")

        viewModelScope.launch {
            if (currentUserId != null) {
                val userHistoryLiveData = userCacheRepository.getCachesFoundByUser(userId = currentUserId)
                userHistoryLiveData.observeForever { history ->
                    Log.d("CacheHistoryViewModel", "History size: ${history?.size ?: 0}")
                    Log.d("CacheHistoryViewModel", "History contents: $history")

                    if (history.isNullOrEmpty()) {
                        _errorMessage.value = "You haven't found any caches yet! Go hunting and come back later! 🔆"
                    } else {
                        _userHistory.postValue(history)
                        _errorMessage.value = null
                    }
                }
            } else {
                _errorMessage.value = "Something went wrong!"
            }
        }
    }

}


class CacheHistoryViewModelFactory(
    private val application: Application,
    private val userCacheRepository: UserCacheCrossRefRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CacheHistoryViewModel::class.java)) {
            Log.d("ViewModelFactory", "Creating CacheHistoryViewModel")
            return CacheHistoryViewModel(application, userCacheRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}