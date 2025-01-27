package com.cmu.estg.cmu_geocaching.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["createdBy"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Cache(
    @PrimaryKey
    var cacheId: String,
    var createdBy: String? = null, // Keep nullable if creator info might come from Firebase
    var latitude: String,
    var longitude: String,
    var difficulty: String,
    var image: String? = null, // Nullable if images are optional
    var rating: Int = 0,
    var description: String
) {
    constructor() : this("", "", "", "", "", "", 0, "")
    constructor(cacheId: String, createdBy: String, difficulty: String, latitude: String, longitude: String, rating: String, image: String, description: String) : this()
    ;

}

