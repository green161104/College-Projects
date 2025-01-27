package ipp.estg.mobile.data.retrofit.leaflingsEndpoints

import ipp.estg.mobile.data.retrofit.models.userPlant.UserPlantRequest
import ipp.estg.mobile.data.retrofit.models.userPlant.UserPlantResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserPlantApi {
    @POST("api/UserPlants/")
    fun createUserPlant(
        @Body userPlant: UserPlantRequest,
    ): Call<Unit>

    @GET("api/UserPlants/{userId}")
    fun getUserPlants(
        @Path("userId") userId: Int,
    ): Call<List<UserPlantResponse>>

    @DELETE("api/UserPlants/{userId}/{plantId}")
    fun deleteUserPlant(
        @Path("userId") userId: Int,
        @Path("plantId") plantId: Int,
    ): Call<Unit>
}