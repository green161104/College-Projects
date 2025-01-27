package ipp.estg.mobile.data.retrofit.leaflingsEndpoints

import ipp.estg.mobile.data.retrofit.models.diaryLogs.LogRequest
import ipp.estg.mobile.data.retrofit.models.diaryLogs.LogResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LogApi {
    @GET("api/Log/diary/{diaryId}")
    fun getLogs(
        @Path("diaryId") diaryId: Int
    ): Call<List<LogResponse>>

    @GET("api/Log/{id}")
    fun getLog(
        @Path("id") id: Int
    ): Call<LogResponse>

    @POST("api/Log")
    fun addLog(
        @Body log: LogRequest
    ): Call<Unit>

    @PUT("api/Log/{id}")
    fun updateLog(
        @Path("id") id: Int,
        @Body log: LogRequest
    ): Call<Unit>

    @DELETE("api/Log/{id}")
    fun deleteLog(
        @Path("id") id: Int
    ): Call<Unit>
}