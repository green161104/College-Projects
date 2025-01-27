package ipp.estg.mobile.data.retrofit.models.user

data class UserResponse(
    val id: Int,
    val username: String,
    val rolePaid: Boolean,
    val contact: String,
    val location: String,
    val careExperience: String,
    val luminosityAvailability: String,
    val waterAvailability: String,
    val userAvatar: String
)