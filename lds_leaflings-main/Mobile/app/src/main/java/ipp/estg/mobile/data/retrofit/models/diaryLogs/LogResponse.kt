package ipp.estg.mobile.data.retrofit.models.diaryLogs

data class LogResponse(
    val id: Int,
    val diaryId: Int,
    val logDate: String,
    val logDescription: String
)