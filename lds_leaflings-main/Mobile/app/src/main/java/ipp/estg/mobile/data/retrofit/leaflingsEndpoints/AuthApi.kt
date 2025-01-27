package ipp.estg.mobile.data.retrofit.leaflingsEndpoints

import ipp.estg.mobile.data.retrofit.models.login.LoginRequest
import ipp.estg.mobile.data.retrofit.models.login.LoginResponse
import ipp.estg.mobile.data.retrofit.models.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApi {
    @POST("api/Auth/login")
    fun login(@Body input: LoginRequest): Call<LoginResponse>

    @Multipart
    @POST("api/User")
    fun register(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("contact") contact: RequestBody,
        @Part("location") location: RequestBody,
        @Part("careExperience") careExperience: RequestBody,
        @Part("luminosityAvailability") luminosityAvailability: RequestBody,
        @Part("waterAvailability") waterAvailability: RequestBody,
        @Part userAvatar: MultipartBody.Part?
    ): Call<RegisterResponse>

    @GET("api/Auth/{authToken}")
    fun validateToken(
        @Path("authToken") authToken: String
    ): Call<LoginResponse>
}