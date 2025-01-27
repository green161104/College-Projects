package ipp.estg.mobile.data.retrofit.models.user

import okhttp3.MultipartBody

data class UserRequest(
    val username: String,
    val email: String,
    val password: String,
    val contact: String,
    val location: String,
    val careExperience: String,
    val luminosityAvailability: String,
    val waterAvailability: String,
    val userAvatar: MultipartBody.Part?
)