package com.cmu.estg.cmu_geocaching.data.local.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cmu.estg.cmu_geocaching.data.local.dao.CacheDao
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class CacheRepository(private val cacheDao: CacheDao, private val firestore: FirebaseFirestore,) {

    // Insert a cache
    suspend fun upsertCache(cache: Cache) {
        cacheDao.upsertCache(cache)

        // Save to Firestore
        try {
            val cacheData = hashMapOf(
                "cacheId" to cache.cacheId,
                "createdBy" to cache.createdBy,
                "difficulty" to cache.difficulty,
                "latitude" to cache.latitude,
                "longitude" to cache.longitude,
                "rating" to cache.rating,
                "image" to cache.image,
                "description" to cache.description
            )

            // Add or update the cache document in Firestore
            firestore.collection("Caches")
                .document(cache.cacheId) // Use cacheId as the Firestore document ID
                .set(cacheData)
                .await()

        } catch (e: Exception) {
            println("Error saving cache to Firestore: ${e.message}")
        }
    }


    // Get a cache by ID
    fun getCacheById(cacheId: String): LiveData<Cache?> {
        return cacheDao.getCacheById(cacheId)
    }

    suspend fun updateCache(cache: Cache) {
        cacheDao.upsertCache(cache)
    }

    // Delete a cache
    suspend fun deleteCache(cache: Cache) {
        cacheDao.deleteCache(cache)
    }

    //get all cache
     fun getAllCaches(): LiveData<List<Cache>> {
        return cacheDao.getAllCaches()
    }

    fun getAllCachesFromFirestore(): LiveData<List<Cache>> {
        val cachesLiveData = MutableLiveData<List<Cache>>() // Mutable LiveData to hold the data

        // Get reference to the Firestore collection
        firestore.collection("Caches")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Log the error
                    Log.e("Firestore", "Error fetching caches", e)
                    cachesLiveData.value = emptyList() // Set to empty list on error
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    try {
                        // Map the documents to Cache objects
                        val caches = snapshots.documents.mapNotNull { document ->
                            document.toObject(Cache::class.java)?.apply {
                                this.cacheId = document.id // Add Firestore document ID
                            }
                        }
                        cachesLiveData.value = caches // Update the LiveData
                        Log.d("CACHE REPOSITORY", caches.toString())
                    } catch (exception: Exception) {
                        Log.e("Firestore", "Error parsing cache documents", exception)
                        cachesLiveData.value = emptyList()
                    }
                }
            }

        return cachesLiveData // Return the LiveData to observers
    }

    fun updateCacheInFirestore(cacheId: String, newRating: Int): Task<Void> {
        val cacheRef = firestore.collection("Caches").document(cacheId)
        return cacheRef.update("rating", newRating)
    }

    fun deleteCacheFromFirestore(cacheId: String): Task<Void> {
        return firestore.collection("Caches").document(cacheId).delete()
    }

}
