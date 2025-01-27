package com.cmu.estg.cmu_geocaching.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.cmu.estg.cmu_geocaching.data.local.entities.User

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM User")
    fun getAllUsers(): LiveData<List<User>>

    @Query("UPDATE User SET profilePicture = :picturePath WHERE userId = :userId")
    suspend fun updateProfilePicturePath(userId: String, picturePath: String)


}
