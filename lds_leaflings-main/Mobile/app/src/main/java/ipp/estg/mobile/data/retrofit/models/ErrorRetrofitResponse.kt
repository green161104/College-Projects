package ipp.estg.mobile.data.retrofit.models

import android.telecom.Call.Details

data class ErrorRetrofitResponse(
    val error: String,
    val details: Details?
)