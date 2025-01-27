package ipp.estg.mobile.data.repositories

import ipp.estg.mobile.data.retrofit.LeaflingsApi
import ipp.estg.mobile.data.retrofit.models.GetAllGenericResponse
import ipp.estg.mobile.data.retrofit.models.plant.PlantResponse
import ipp.estg.mobile.data.retrofit.models.task.TaskResponse
import ipp.estg.mobile.utils.Resource

/**
 * Repository for handling operations related to plants.
 *
 * @property leaflingsApi An instance of the API service for performing plant-related network operations.
 */
class PlantRepository(
    private val leaflingsApi: LeaflingsApi
) : BaseRepository() {

    /**
     * Fetches a list of all plants from the Leaflings API.
     *
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [GetAllGenericResponse] or an error message.
     */
    fun getPlants(
        callback: (Resource<GetAllGenericResponse<PlantResponse>>) -> Unit
    ) {
        val call = leaflingsApi.getPlants()
        handleResponse(call, callback)
    }

    /**
     * Fetches details of a specific plant by its ID.
     *
     * @param plantId The ID of the plant to retrieve.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [PlantResponse] or an error message.
     */
    fun getPlantById(
        plantId: Int,
        callback: (Resource<PlantResponse>) -> Unit
    ) {
        val call = leaflingsApi.getPlantById(plantId)
        handleResponse(call, callback)
    }

    /**
     * Fetches the tasks associated with a specific plant by its ID.
     *
     * @param plantId The ID of the plant whose tasks are to be retrieved.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [GetAllGenericResponse] or an error message.
     */
    fun getPlantTasks(
        plantId: Int,
        callback: (Resource<GetAllGenericResponse<TaskResponse>>) -> Unit
    ) {
        val call = leaflingsApi.getPlantTasks(plantId)
        handleResponse(call, callback)
    }
}