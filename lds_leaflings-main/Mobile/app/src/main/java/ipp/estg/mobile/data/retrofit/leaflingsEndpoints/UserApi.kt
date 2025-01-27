package ipp.estg.mobile.data.retrofit.leaflingsEndpoints

import ipp.estg.mobile.data.retrofit.models.user.MatchResponse
import ipp.estg.mobile.data.retrofit.models.user.UpdateImageResponse
import ipp.estg.mobile.data.retrofit.models.user.UserPasswordRequest
import ipp.estg.mobile.data.retrofit.models.user.UserPreferencesRequest
import ipp.estg.mobile.data.retrofit.models.user.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {

    @GET("api/User/{id}")
    fun getUserInfo(
        @Path("id") id: Int,
    ): Call<UserResponse>

    @PUT("api/User/preferences/{id}")
    fun updateUserPreferences(
        @Path("id") id: Int,
        @Body userPreferencesRequest: UserPreferencesRequest,
    ): Call<Unit>

    @GET("api/User/match/{userId}")
    fun getMatchingPlants(
        @Path("userId") userId: Int,
    ): Call<MatchResponse>

    @PUT("api/User/password/{id}")
    fun updatePassword(
        @Path("id") id: Int,
        @Body userPasswordRequest: UserPasswordRequest,
    ): Call<Unit>

    @Multipart
    @PUT("api/User/image/{id}")
    fun updateImage(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part
    ): Call<UpdateImageResponse>

}