package com.cmu.estg.cmu_geocaching.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.cmu.estg.cmu_geocaching.data.local.entities.User
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.CacheWithUsers
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.UserCacheCrossRef
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.UserWithCaches

@Dao
interface UserCacheCrossRefDao {
    @Upsert
    suspend fun upsertUserCacheCrossRef(crossRef: UserCacheCrossRef)

    @Delete
    suspend fun deleteUserCacheCrossRef(crossRef: UserCacheCrossRef)

    @Query("SELECT * FROM UserCacheCrossRef WHERE userId = :userId")
     fun getCachesFoundByUser(userId: String): LiveData<List<UserCacheCrossRef>>

    @Query("SELECT * FROM UserCacheCrossRef WHERE cacheId = :cacheId")
     fun getUsersWhoFoundCache(cacheId: Int): LiveData<List<UserCacheCrossRef>>

    @Transaction
    @Query("SELECT * FROM User WHERE userId = :userId")
     fun getUserWithCaches(userId: String): LiveData<UserWithCaches >//fetches the caches associated with the user

    @Transaction
    @Query("SELECT * FROM Cache WHERE cacheId = :cacheId")
     fun getCacheWithUsers(cacheId: Int): LiveData<CacheWithUsers >//fetches the users associated with cache><>

    @Transaction
    @Query("SELECT * FROM User")
     fun getAllUsersWithCaches(): LiveData<List<UserWithCaches>>

    @Transaction
    @Query("SELECT * FROM Cache")
     fun getAllCachesWithUsers(): LiveData<List<CacheWithUsers>>

     @Query("SELECT * FROM UserCacheCrossRef")
     fun getAllUserCacheCrossRef(): LiveData<List<UserCacheCrossRef>>

    @Query("DELETE FROM UserCacheCrossRef WHERE cacheId = :cacheId")
    suspend fun deleteByCacheId(cacheId: String)

    @Query("DELETE FROM UserCacheCrossRef WHERE userId = :userId AND cacheId = :cacheId")
    suspend fun deleteByUserAndCacheId(userId: String, cacheId: String)
}
