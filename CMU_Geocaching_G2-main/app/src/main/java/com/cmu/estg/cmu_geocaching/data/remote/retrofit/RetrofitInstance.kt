package com.cmu.estg.cmu_geocaching.data.remote.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    val api: TriviaApi by lazy{
        Retrofit.Builder()
            .baseUrl("https://the-trivia-api.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TriviaApi::class.java)
    }
}
