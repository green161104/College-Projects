package com.cmu.estg.cmu_geocaching.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class User(
    @PrimaryKey var userId: String,
    var name: String,
    var email: String,
    var pronouns: String,
    var points: Int = 0,
    var profilePicture: String = ""

){
   constructor() : this("", "", "", "", 0, "")
}