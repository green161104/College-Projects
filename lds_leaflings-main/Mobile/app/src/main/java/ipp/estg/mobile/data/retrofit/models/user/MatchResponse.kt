package ipp.estg.mobile.data.retrofit.models.user

import ipp.estg.mobile.data.retrofit.models.plant.PlantResponse

data class MatchResponse(
    val averageMatches: List<PlantResponse>,
    val perfectMatches: List<PlantResponse>,
    val weakMatches: List<PlantResponse>
)