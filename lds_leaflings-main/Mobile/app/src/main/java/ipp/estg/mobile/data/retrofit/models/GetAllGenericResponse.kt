package ipp.estg.mobile.data.retrofit.models

data class GetAllGenericResponse<T>(
    val data: List<T>,
    val total: Int
)