package ipp.estg.mobile.data.repositories

import ipp.estg.mobile.data.retrofit.LeaflingsApi
import ipp.estg.mobile.data.retrofit.models.diary.DiaryRequest
import ipp.estg.mobile.data.retrofit.models.diary.DiaryResponse
import ipp.estg.mobile.data.retrofit.models.diaryLogs.LogRequest
import ipp.estg.mobile.data.retrofit.models.diaryLogs.LogResponse
import ipp.estg.mobile.utils.Resource

/**
 * Repository for handling diary and log-related operations.
 *
 * @property leaflingsApi An instance of the API service for performing diary and log-related network operations.
 */
class DiaryRepository(
    private val leaflingsApi: LeaflingsApi
) : BaseRepository(
) {
    /**
     * Retrieves a diary for a specific user plant.
     *
     * @param userPlantId The ID of the user plant associated with the diary.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [DiaryResponse] or an error message.
     */
    fun getDiary(
        userPlantId: Int,
        callback: (Resource<DiaryResponse>) -> Unit
    ) {
        val call = leaflingsApi.getDiary(userPlantId)
        handleResponse(call, callback)
    }

    /**
     * Adds a new diary entry.
     *
     * @param diary The [DiaryRequest] containing the diary data to be added.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [DiaryResponse] or an error message.
     */
    fun addDiary(
        diary: DiaryRequest,
        callback: (Resource<DiaryResponse>) -> Unit
    ) {
        val call = leaflingsApi.addDiary(diary)
        handleResponse(call, callback)
    }

    /**
     * Retrieves logs associated with a specific diary.
     *
     * @param diaryId The ID of the diary.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful list of [LogResponse] or an error message.
     */
    fun getLogs(
        diaryId: Int,
        callback: (Resource<List<LogResponse>>) -> Unit
    ) {
        val call = leaflingsApi.getLogs(diaryId)
        handleResponse(call, callback)
    }

    /**
     * Retrieves a specific log by its ID.
     *
     * @param id The ID of the log.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [LogResponse] or an error message.
     */
    fun getLog(
        id: Int,
        callback: (Resource<LogResponse>) -> Unit
    ) {
        val call = leaflingsApi.getLog(id)
        handleResponse(call, callback)
    }

    /**
     * Adds a new log entry to a diary.
     *
     * @param log The [LogRequest] containing the log data to be added.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful result or an error message.
     */
    fun addLog(
        log: LogRequest,
        callback: (Resource<Unit>) -> Unit
    ) {
        val call = leaflingsApi.addLog(log)
        handleResponse(call, callback)
    }

    /**
     * Updates an existing log entry.
     *
     * @param id The ID of the log to update.
     * @param log The [LogRequest] containing the updated log data.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful result or an error message.
     */
    fun updateLog(
        id: Int,
        log: LogRequest,
        callback: (Resource<Unit>) -> Unit
    ) {
        val call = leaflingsApi.updateLog(id, log)
        handleResponse(call, callback)
    }

    /**
     * Deletes a specific log entry.
     *
     * @param id The ID of the log to delete.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful result or an error message.
     */
    fun deleteLog(
        id: Int,
        callback: (Resource<Unit>) -> Unit
    ) {
        val call = leaflingsApi.deleteLog(id)
        handleResponse(call, callback)
    }
}