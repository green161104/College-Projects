package ipp.estg.mobile.data.retrofit.leaflingsEndpoints

import ipp.estg.mobile.data.retrofit.models.GetAllGenericResponse
import ipp.estg.mobile.data.retrofit.models.plant.PlantResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PlantApi {
    @GET("api/Plant")
    fun getPlants(): Call<GetAllGenericResponse<PlantResponse>>

    @GET("api/Plant/{id}")
    fun getPlantById(
        @Path("id") id: Int
    ): Call<PlantResponse>
}