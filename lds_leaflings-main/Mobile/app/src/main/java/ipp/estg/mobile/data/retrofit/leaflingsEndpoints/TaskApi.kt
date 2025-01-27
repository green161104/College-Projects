package ipp.estg.mobile.data.retrofit.leaflingsEndpoints

import ipp.estg.mobile.data.retrofit.models.GetAllGenericResponse
import ipp.estg.mobile.data.retrofit.models.task.TaskResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskApi {
    @GET("api/Task/plant/{id}")
    fun getPlantTasks(
        @Path("id") id: Int
    ): Call<GetAllGenericResponse<TaskResponse>>
}