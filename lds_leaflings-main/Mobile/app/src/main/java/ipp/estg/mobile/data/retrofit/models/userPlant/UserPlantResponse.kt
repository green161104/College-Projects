package ipp.estg.mobile.data.retrofit.models.userPlant

data class UserPlantResponse(
    val id: Int,
    val userId: Int,
    val plant: ipp.estg.mobile.data.retrofit.models.plant.PlantResponse
)