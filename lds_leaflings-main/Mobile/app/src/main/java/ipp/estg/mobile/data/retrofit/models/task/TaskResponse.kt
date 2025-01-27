package ipp.estg.mobile.data.retrofit.models.task

data class TaskResponse(
    val adminId: Int,
    val id: Int,
    val plantId: Int,
    val taskDescription: String,
    val taskName: String
)