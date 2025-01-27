package com.cmu.estg.cmu_geocaching.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cmu.estg.cmu_geocaching.data.local.GeocachingDatabase
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.data.local.repository.CacheRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedCacheViewModel(
    application: Application,
    private val cacheRepository: CacheRepository,
    private val database: GeocachingDatabase
) : AndroidViewModel(application) {
    private val _selectedCache = MutableStateFlow<Cache?>(null)
    val selectedCache: StateFlow<Cache?> = _selectedCache
    private val _allCaches = MutableLiveData<List<Cache>>()
    val allCaches: LiveData<List<Cache>> get() = _allCaches

    init{
        getAllCachesFirestore()
    }

    fun setSelectedCache(cache: Cache?) {
        _selectedCache.value = cache

    }

    private fun getAllCachesFirestore() {
        // Observe the LiveData from the repository
        cacheRepository.getAllCachesFromFirestore().observeForever { caches ->
            Log.d("getAllCachesFirestore VIEWMODEL", caches.toString())
            _allCaches.value = caches ?: emptyList() // Update the internal LiveData
        }
    }

    fun updateCacheRating(newRating: Int) {
        // Update in Firestore
        _selectedCache.value?.let { cache ->
            cacheRepository.updateCacheInFirestore(cache.cacheId, newRating)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewModelScope.launch {
                            if (newRating <= 0) {
                                // Delete cache from both Firestore and Room
                                cacheRepository.deleteCacheFromFirestore(cache.cacheId)
                                database.deleteCacheWithRelations(cache)
                                Log.d("updateCacheRating", "Cache deleted from both Firestore and Room")

                                // Reset selected cache
                                _selectedCache.value = null
                            } else {
                                // Update cache rating in Room
                                val updatedCache = cache.copy(rating = newRating)
                                cacheRepository.upsertCache(updatedCache)

                                // Update the selected cache state
                                _selectedCache.value = null

                                Log.d("updateCacheRating", "Cache rating updated in Room")
                            }
                        }
                    } else {
                        Log.e("updateCacheRating", "Failed to update cache in Firestore", task.exception)
                    }
                }
        }
    }


    class SharedCacheViewModelFactory(
        private val application: Application,
        private val cacheRepository: CacheRepository,
        private val database: GeocachingDatabase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SharedCacheViewModel::class.java)) {
                Log.d("ViewModelFactory", "Creating SharedCacheViewModel")
                return SharedCacheViewModel(application, cacheRepository, database) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}
