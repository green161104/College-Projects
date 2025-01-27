package ipp.estg.mobile.data.retrofit.models.plant

data class PlantResponse(
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val luminosityNeeded: String,
    val waterNeeds: String,
    val expSuggested: String,
    val plantImage: String
)