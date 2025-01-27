package com.cmu.estg.cmu_geocaching.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import com.cmu.estg.cmu_geocaching.data.local.dao.CacheDao
import com.cmu.estg.cmu_geocaching.data.local.dao.UserCacheCrossRefDao
import com.cmu.estg.cmu_geocaching.data.local.dao.UserDao
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.data.local.entities.User
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.Converters
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.UserCacheCrossRef

@Database(
    entities = [User::class, Cache::class, UserCacheCrossRef::class],
    version = 9,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class GeocachingDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cacheDao(): CacheDao
    abstract fun userCacheCrossRefDao(): UserCacheCrossRefDao

    suspend fun deleteCacheWithRelations(cache: Cache) {
        withTransaction {
            userCacheCrossRefDao().deleteByCacheId(cache.cacheId)
            cacheDao().deleteCache(cache)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GeocachingDatabase? = null

        fun getDatabase(context: Context): GeocachingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GeocachingDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }


    }
}

