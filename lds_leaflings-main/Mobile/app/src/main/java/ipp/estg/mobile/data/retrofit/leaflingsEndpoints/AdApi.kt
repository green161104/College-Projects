package ipp.estg.mobile.data.retrofit.leaflingsEndpoints

import ipp.estg.mobile.data.retrofit.models.ad.AdResponse
import retrofit2.Call
import retrofit2.http.GET

interface AdApi{

    @GET("api/Ad/random")
    fun getRandomAd(): Call<AdResponse>

}