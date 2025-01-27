package ipp.estg.mobile.data.retrofit.models.register

import ipp.estg.mobile.data.enums.CareExperience
import ipp.estg.mobile.data.enums.LuminosityAvailability
import ipp.estg.mobile.data.enums.WaterAvailability
import java.io.File

data class RegisterRequest(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val location: String = "",
    val contact: String = "",
    val careExperience: String = CareExperience.Beginner.value, // default values
    val waterAvailability: String = WaterAvailability.LOW.value,
    val luminosityAvailability: String = LuminosityAvailability.LOW.value,
    val userAvatar: File? = null
)