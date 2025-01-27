package ipp.estg.mobile.data.repositories

import android.content.Context
import android.net.Uri
import ipp.estg.mobile.data.preferences.AuthPreferences
import ipp.estg.mobile.data.retrofit.LeaflingsApi
import ipp.estg.mobile.data.retrofit.models.user.MatchResponse
import ipp.estg.mobile.data.retrofit.models.user.UpdateImageResponse
import ipp.estg.mobile.data.retrofit.models.user.UserPasswordRequest
import ipp.estg.mobile.data.retrofit.models.user.UserPreferencesRequest
import ipp.estg.mobile.data.retrofit.models.user.UserResponse
import ipp.estg.mobile.utils.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * Repository for handling user-related operations.
 *
 * @property context The application context for file and resource operations.
 * @property leaflingsApi An instance of the API service for performing user-related network operations.
 * @property authPreferences An instance of [AuthPreferences] for accessing user authentication data.
 */
class UserRepository(
    private val context: Context,
    private val leaflingsApi: LeaflingsApi,
    private val authPreferences: AuthPreferences
) : BaseRepository() {

    /**
     * Retrieves information about the currently logged-in user.
     *
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [UserResponse] or an error message.
     */
    fun getUserInfo(
        callback: (Resource<UserResponse>) -> Unit
    ) {
        val userId = authPreferences.getUserId()
        val call = leaflingsApi.getUserInfo(userId)
        handleResponse(call, callback)
    }

    /**
     * Updates the user's preferences.
     *
     * @param userPreferencesRequest The [UserPreferencesRequest] containing the updated preferences.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful result or an error message.
     */
    fun updateUserPreferences(
        userPreferencesRequest: UserPreferencesRequest,
        callback: (Resource<Unit>) -> Unit
    ) {
        val userId = authPreferences.getUserId()
        val call = leaflingsApi.updateUserPreferences(userId, userPreferencesRequest)
        handleResponse(call, callback)
    }

    /**
     * Updates the user's password.
     *
     * @param userPasswordRequest The [UserPasswordRequest] containing the new password details.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful result or an error message.
     */
    fun updatePassword(
        userPasswordRequest: UserPasswordRequest,
        callback: (Resource<Unit>) -> Unit
    ) {
        val userId = authPreferences.getUserId()
        val call = leaflingsApi.updatePassword(userId,userPasswordRequest )
        handleResponse(call, callback)
    }

    /**
     * Updates the user's profile image.
     *
     * @param imageUri The URI of the new profile image.
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [UpdateImageResponse] or an error message.
     */
    fun updateImage(
        imageUri: String,
        callback: (Resource<UpdateImageResponse>) -> Unit
    ) {
        try {
            val userId = authPreferences.getUserId()

            // Convert the image URI to a File
            val uri = Uri.parse(imageUri)
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_image.jpg")
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // Create the MultipartBody.Part
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

            // Make the API call
            val call = leaflingsApi.updateImage(userId, imagePart)
            handleResponse(call, callback)
        } catch (e: Exception) {
            callback(Resource.Error("Error processing image: ${e.message}"))
        }
    }

    /**
     * Retrieves plants that match the user's preferences or attributes.
     *
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [MatchResponse] or an error message.
     */
    fun getMatchingPlants(
        callback: (Resource<MatchResponse>) -> Unit
    ) {
        val userId = authPreferences.getUserId()
        val call = leaflingsApi.getMatchingPlants(userId)
        handleResponse(call, callback)
    }

}