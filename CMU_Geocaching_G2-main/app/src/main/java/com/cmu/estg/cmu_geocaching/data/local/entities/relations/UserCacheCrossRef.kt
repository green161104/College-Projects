package com.cmu.estg.cmu_geocaching.data.local.entities.relations

import androidx.room.Entity
import androidx.room.TypeConverter
import java.util.Date

@Entity(primaryKeys = ["userId", "cacheId"])
data class UserCacheCrossRef(

    val userId: String,
    val cacheId: String,
    val dateFound: Date,
    val pointsEarned: Int

)


class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}