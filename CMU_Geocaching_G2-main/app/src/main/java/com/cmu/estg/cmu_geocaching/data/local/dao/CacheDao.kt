package com.cmu.estg.cmu_geocaching.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.CacheWithUsers

@Dao
interface CacheDao {
    @Upsert
    suspend fun upsertCache(cache: Cache)

    @Delete
    suspend fun deleteCache(cache: Cache)

    @Query("SELECT * FROM Cache WHERE cacheId = :cacheId")
    fun getCacheById(cacheId: String): LiveData<Cache?>

    @Query("SELECT * FROM Cache")
    fun getAllCaches(): LiveData<List<Cache>>



}
