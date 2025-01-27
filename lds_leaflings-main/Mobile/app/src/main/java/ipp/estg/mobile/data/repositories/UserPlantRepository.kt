package ipp.estg.mobile.data.repositories

import ipp.estg.mobile.data.preferences.AuthPreferences
import ipp.estg.mobile.data.retrofit.LeaflingsApi
import ipp.estg.mobile.data.retrofit.models.userPlant.UserPlantRequest
import ipp.estg.mobile.data.retrofit.models.userPlant.UserPlantResponse
import ipp.estg.mobile.utils.Resource

/**
 * Repository for handling user plant-related operations.
 *
 * @property leaflingsApi An instance of the API service for performing user plant-related network operations.
 * @property authPreferences An instance of [AuthPreferences] for accessing user authentication data.
 */
class UserPlantRepository(
    private val leaflingsApi: LeaflingsApi,
    private val authPreferences: AuthPreferences
) : BaseRepository() {

    /**
     * Retrieves the list of plants associated with the current user.
     *
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful list of [UserPlantResponse] or an error message.
     */
    fun getUserPlants(
        callback: (Resource<List<UserPlantResponse>>) -> Unit
    ) {
        val userId = authPreferences.getUserId()
        val call = leaflingsApi.getUserPlants(userId)
        handleResponse(call, callback)
    }

    /**
     * Adds a new plant to the user's collection.
     *
     * @param plantId The ID of the plant to add.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful result or an error message.
     */
    fun createUserPlant(
        plantId: Int,
        callback: (Resource<Unit>) -> Unit
    ) {
        val userId = authPreferences.getUserId()
        val newUserPlant = UserPlantRequest(
            userId = userId,
            plantId = plantId
        )
        val call = leaflingsApi.createUserPlant(newUserPlant)
        handleResponse(call, callback)
    }

    /**
     * Removes a plant from the user's collection.
     *
     * @param plantId The ID of the plant to remove.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful result or an error message.
     */
    fun deleteUserPlant(
        plantId: Int,
        callback: (Resource<Unit>) -> Unit
    ) {
        val userId = authPreferences.getUserId()
        val call = leaflingsApi.deleteUserPlant(userId, plantId)
        handleResponse(call, callback)
    }
}