package com.cmu.estg.cmu_geocaching.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.data.local.entities.User

data class UserWithCaches(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "cacheId",
        associateBy = Junction(UserCacheCrossRef::class)
    )
    val cachesFound: List<Cache>
)

