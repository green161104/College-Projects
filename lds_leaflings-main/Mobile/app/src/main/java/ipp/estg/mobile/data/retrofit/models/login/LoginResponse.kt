package ipp.estg.mobile.data.retrofit.models.login

data class LoginResponse(
    val auth: Boolean,
    val expiration: String,
    val token: String,
    val userId: Int,
    val rolePaid: Boolean
)