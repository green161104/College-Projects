package com.cmu.estg.cmu_geocaching.data.remote.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface TriviaApi {
    @GET("questions")
    suspend fun getQuestions(): Response<List<TriviaQuestion>>
}