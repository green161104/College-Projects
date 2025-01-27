package ipp.estg.mobile.data.repositories

import com.google.gson.Gson
import ipp.estg.mobile.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Abstract base repository providing common functionality for handling API responses.
 */
abstract class BaseRepository {

    /**
     * Handles the response of an API call and processes it into a [Resource].
     *
     * @param T The type of the response body.
     * @param call The Retrofit [Call] object to execute the API request.
     * @param callback A lambda function to handle the processed [Resource] result.
     */
    fun <T> handleResponse(
        call: Call<T>,
        callback: (Resource<T>) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                if (response.isSuccessful && response.code() in 200..299) {
                    callback(Resource.Success(response.body()))
                } else {
                    val responseMessage = generateErrorMessage(response)
                    callback(Resource.Error(responseMessage))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback(Resource.Error("An unknown error occurred: ${t.localizedMessage}"))
            }
        })
    }


    /**
     * Generates an error message based on the API response.
     *
     * @param response The [Response] object from the API call.
     * @return A string representing the error message.
     */
    private fun generateErrorMessage(response: Response<*>): String {
        val errorResponse = response.errorBody()?.string() ?: return "Unknown error"

        when (response.code()) {
            401 -> return "User is not authenticated"
            else -> {
                try {
                    val errorDto = Gson().fromJson(errorResponse, ipp.estg.mobile.data.retrofit.models.ErrorRetrofitResponse::class.java)
                    return errorDto?.error ?: "Unknown error"
                } catch (e: Exception) {
                    return "Unknown error"
                }
            }
        }

    }
}