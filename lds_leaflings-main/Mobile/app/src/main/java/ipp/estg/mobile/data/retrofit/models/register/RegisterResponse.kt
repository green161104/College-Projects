package ipp.estg.mobile.data.retrofit.models.register

data class RegisterResponse(
    val username: String,
    val contact: String,
    val location: String,
    val careExperience: String,
    val luminosityAvailability: String,
    val waterAvailability: String,
    val userAvatar: String?
)