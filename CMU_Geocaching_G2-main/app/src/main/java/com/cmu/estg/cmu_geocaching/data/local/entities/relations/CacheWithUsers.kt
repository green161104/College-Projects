package com.cmu.estg.cmu_geocaching.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.data.local.entities.User

data class CacheWithUsers(
    @Embedded val cache: Cache,
    @Relation(
        parentColumn = "cacheId",
        entityColumn = "userId",
        associateBy = Junction(UserCacheCrossRef::class)
    )
    val usersFound: List<User>
)
