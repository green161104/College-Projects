package ipp.estg.mobile.data.repositories

import com.google.gson.Gson
import ipp.estg.mobile.data.preferences.AuthPreferences
import ipp.estg.mobile.data.retrofit.LeaflingsApi
import ipp.estg.mobile.data.retrofit.models.ErrorRetrofitResponse
import ipp.estg.mobile.data.retrofit.models.login.LoginRequest
import ipp.estg.mobile.data.retrofit.models.login.LoginResponse
import ipp.estg.mobile.data.retrofit.models.register.RegisterRequest
import ipp.estg.mobile.data.retrofit.models.register.RegisterResponse
import ipp.estg.mobile.utils.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository for handling authentication-related operations including login, register,
 * token validation, and logout. This class interacts with the Leaflings API
 * and manages authentication preferences.
 *
 * @property leaflingsApi An instance of the API service for performing network operations.
 * @property authPreferences An instance of preferences storage for authentication details.
 */
class AuthRepository(
    private val leaflingsApi: LeaflingsApi,
    private val authPreferences: AuthPreferences
) : BaseRepository() {


    /**
     * Registers a new user by sending their details to the Leaflings API.
     *
     * @param input A [RegisterRequest] containing user registration details.
     * @param callback A lambda function to handle the response, providing a [Resource]
     *        with either the successful response or an error message.
     */
    fun register(
        input: RegisterRequest,
        callback: (Resource<RegisterResponse>) -> Unit
    ) {
        // Create RequestBody objects
        val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), input.username)
        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), input.email)
        val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), input.password)
        val contactBody = RequestBody.create("text/plain".toMediaTypeOrNull(), input.contact)
        val locationBody = RequestBody.create("text/plain".toMediaTypeOrNull(), input.location)
        val careExperienceBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), input.careExperience)
        val waterAvailabilityBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), input.waterAvailability)
        val luminosityAvailabilityBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), input.luminosityAvailability)

        // Handle file upload if provided
        val avatarPart = if (input.userAvatar != null) {
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), input.userAvatar)
            MultipartBody.Part.createFormData("UserAvatar", input.userAvatar.name, requestFile)
        } else {
            val emptyFile = RequestBody.create("image/*".toMediaTypeOrNull(), ByteArray(0))
            MultipartBody.Part.createFormData("UserAvatar", "", emptyFile)
        }

        // Make the API call
        val call = leaflingsApi.register(
            usernameBody, emailBody, passwordBody, contactBody,
            locationBody, careExperienceBody, waterAvailabilityBody,
            luminosityAvailabilityBody, avatarPart
        )

        handleResponse(call, callback)
    }


    /**
     * Logs in a user by verifying their credentials with the Leaflings API.
     * If successful, saves the authentication token and user details in preferences.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     * @param callback A lambda function to handle the response, providing a [Resource]
     *        with either the successful response or an error message.
     */
    fun login(
        email: String,
        password: String,
        callback: (Resource<LoginResponse>) -> Unit
    ) {
        val call = leaflingsApi.login(LoginRequest(email, password))

        // Aqui a chamada não é feita a partir do métdo abstrato para poder guardar o token no SharedPreferences
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    // Save the token in the shared preferences
                    val token = response.body()?.token
                    val userId = response.body()?.userId
                    val rolePaid = response.body()?.rolePaid
                    storePreferences(token, userId, rolePaid)

                    // Return the response
                    callback(Resource.Success(response.body()!!))
                } else {
                    val errorResponse = response.errorBody()?.string()
                    errorResponse?.let {
                        val errorDto = Gson().fromJson(it, ErrorRetrofitResponse::class.java)
                        callback(Resource.Error(errorDto.error))
                    } ?: run {
                        callback(Resource.Error("Error: ${response.errorBody()}"))
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(Resource.Error("An unknown error occurred: ${t.localizedMessage}"))
            }
        })
    }

    /**
     * Checks if the user is currently logged in by validating their stored token.
     *
     * @param callback A lambda function to handle the response, providing a [Resource]
     *        with either the successful response or an error message.
     */
    fun isLoggedIn(
        callback: (Resource<LoginResponse>) -> Unit
    ) {
        val authToken = authPreferences.getAuthToken()

        if(authToken.isNullOrEmpty()) {
            callback(Resource.Error("No token found"))
        }

        val call = leaflingsApi.validateToken(authToken!!)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    // Save the token in the shared preferences
                    val token = response.body()?.token
                    val userId = response.body()?.userId
                    val rolePaid = response.body()?.rolePaid
                    storePreferences(token, userId, rolePaid)

                    callback(Resource.Success(response.body()!!))
                } else {
                    val errorResponse = response.errorBody()?.string()
                    errorResponse?.let {
                        val errorDto = Gson().fromJson(it, ErrorRetrofitResponse::class.java)
                        callback(Resource.Error(errorDto.error))
                    } ?: run {
                        callback(Resource.Error("Error: ${response.errorBody()}"))
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(Resource.Error("An unknown error occurred: ${t.localizedMessage}"))
            }
        })
    }

    /**
     * Logs out the user by clearing all stored authentication details.
     */
    fun logout() {
        authPreferences.clear()
    }

    /**
     * Stores authentication details such as token, user ID, and role status in preferences.
     *
     * @param token The authentication token retrieved from the API.
     * @param userId The ID of the user.
     * @param rolePaid Boolean indicating if the user has a paid role.
     */
    private fun storePreferences(token: String?, userId: Int?, rolePaid: Boolean?) {
        if (token != null && userId != null && rolePaid != null) {
            authPreferences.setAuthToken(token)
            authPreferences.setUserId(userId)
            authPreferences.setRolePaid(rolePaid)
        }
    }
}