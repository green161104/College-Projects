package ipp.estg.mobile.data.retrofit.models.ad


data class AdResponse (
    val id: Int,
    val adminID: Int,
    val isActive: Boolean,
    val startDate: String,
    val endDate: String,
    val adFile: String
)