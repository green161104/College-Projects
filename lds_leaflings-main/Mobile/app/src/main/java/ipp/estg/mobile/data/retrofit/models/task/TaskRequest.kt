package ipp.estg.mobile.data.retrofit.models.task

data class TaskRequest(
    val adminId: Int,
    val plantId: Int,
    val taskDescription: String,
    val taskName: String
)