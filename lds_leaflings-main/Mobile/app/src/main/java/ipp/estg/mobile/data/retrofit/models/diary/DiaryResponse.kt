package ipp.estg.mobile.data.retrofit.models.diary

data class DiaryResponse(
    val id: Int,
    val title: String,
    val userPlantId: Int,
    val creationDate: String
)