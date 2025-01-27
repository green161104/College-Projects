package ipp.estg.mobile.data.retrofit.leaflingsEndpoints

import ipp.estg.mobile.data.retrofit.models.diary.DiaryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DiaryApi {
    @GET("api/Diary/userPlant/{userPlantId}")
    fun getDiary(
        @Path("userPlantId") userPlantId: Int
    ): Call<DiaryResponse>

    @POST("api/Diary")
    fun addDiary(
        @Body diary: ipp.estg.mobile.data.retrofit.models.diary.DiaryRequest
    ): Call<DiaryResponse>
}