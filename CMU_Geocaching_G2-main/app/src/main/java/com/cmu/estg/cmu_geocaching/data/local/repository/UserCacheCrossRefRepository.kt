package com.cmu.estg.cmu_geocaching.data.local.repository

import androidx.lifecycle.LiveData
import com.cmu.estg.cmu_geocaching.data.local.dao.UserCacheCrossRefDao
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.UserCacheCrossRef
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.UserWithCaches

class UserCacheCrossRefRepository(private val userCacheCrossRefDao: UserCacheCrossRefDao) {

    // Insert a user-cache cross reference
    suspend fun upsertUserCacheCrossRef(crossRef: UserCacheCrossRef) {
        userCacheCrossRefDao.upsertUserCacheCrossRef(crossRef)
    }

    // Delete a user-cache cross reference
    suspend fun deleteUserCacheCrossRef(crossRef: UserCacheCrossRef) {
        userCacheCrossRefDao.deleteUserCacheCrossRef(crossRef)
    }

    suspend fun getCachesFoundByUser(userId: String): LiveData<List<UserCacheCrossRef>> {
        return userCacheCrossRefDao.getCachesFoundByUser(userId)
    }

    // Get users who found a specific cache
    suspend fun getUsersWhoFoundCache(cacheId: Int): LiveData<List<UserCacheCrossRef>> {
        return userCacheCrossRefDao.getUsersWhoFoundCache(cacheId)
    }

    fun getAllUsersWithCaches(): LiveData<List<UserWithCaches>>{
        return userCacheCrossRefDao.getAllUsersWithCaches()
    }

    fun getAllUserCacheCrossRef(): LiveData<List<UserCacheCrossRef>>{
        return userCacheCrossRefDao.getAllUserCacheCrossRef()
    }
}
