package ipp.estg.mobile.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import ipp.estg.mobile.data.preferences.AuthPreferences
import ipp.estg.mobile.data.repositories.UserRepository
import ipp.estg.mobile.data.retrofit.LeaflingsApiInstance
import ipp.estg.mobile.data.retrofit.models.user.MatchResponse
import ipp.estg.mobile.data.retrofit.models.user.UserPasswordRequest
import ipp.estg.mobile.data.retrofit.models.user.UserPreferencesRequest
import ipp.estg.mobile.data.retrofit.models.user.UserResponse
import ipp.estg.mobile.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userRepository = UserRepository(
        application,
        LeaflingsApiInstance.getLeaflingsApi(application),
        AuthPreferences(application)
    )

    private val _user = MutableStateFlow<UserResponse?>(null)
    var user = _user.asStateFlow()
        private set


    private val _error = MutableStateFlow("")
    var error = _error.asStateFlow()
        private set

    private val _isLoading = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()
        private set

    var imagePath by mutableStateOf<String?>(null)



    fun updateUserPreferences(
        careExperience: String,
        luminosityAvailability: String,
        waterAvailability: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        val userPreferencesRequest =
            UserPreferencesRequest(
                careExperience,
                luminosityAvailability,
                waterAvailability
            )

        _isLoading.value = true
        try {
            userRepository.updateUserPreferences(userPreferencesRequest) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        it.message?.let { it1 -> onError(it1) }
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            _error.value = "Please fill in all fields"
            onError("Please fill in all fields")
            return
        }

        val userPasswordRequest = UserPasswordRequest(
            oldPassword,
            newPassword,
        )

        _isLoading.value = true
        try {
            userRepository.updatePassword(userPasswordRequest) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        it.message?.let { it1 -> onError(it1) }
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun updateImage(imageUri: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {

        _isLoading.value = true

        userRepository.updateImage(imageUri) { result ->
            when (result) {
                is Resource.Success -> {
                    imagePath = result.data?.imagePath
                    _error.value = ""
                    _isLoading.value = false
                    onSuccess()
                }

                is Resource.Error -> {
                    _error.value = "${result.message}"
                    _isLoading.value = false
                    result.message?.let { onError(it) }
                }
            }
        }
    }


    fun getMatchingPlants(
        onSuccess: (MatchResponse) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        try {
            userRepository.getMatchingPlants {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _isLoading.value = false
                        it.data?.let { it1 -> onSuccess(it1) }
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        it.message?.let { it1 -> onError(it1) }
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun getUserInfo(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {

        _isLoading.value = true
        try {
            userRepository.getUserInfo {
                when (it) {
                    is Resource.Success -> {
                        _user.value = it.data
                        _isLoading.value = false
                        _error.value = ""
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        it.message?.let { it1 -> onError(it1) }
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }
}