package ipp.estg.mobile.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ipp.estg.mobile.data.preferences.AuthPreferences
import ipp.estg.mobile.data.repositories.UserPlantRepository
import ipp.estg.mobile.data.retrofit.LeaflingsApiInstance
import ipp.estg.mobile.data.retrofit.models.userPlant.UserPlantResponse
import ipp.estg.mobile.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPlantViewModel( application: Application ) : AndroidViewModel(application) {
    private val userPlantRepository = UserPlantRepository(
        LeaflingsApiInstance.getLeaflingsApi(application),
        AuthPreferences(application)
    )

    private val _error = MutableStateFlow("")
    var error = _error.asStateFlow()
        private set

    private val _isLoading = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()
        private set

    private val _userPlants = MutableStateFlow(emptyList<UserPlantResponse>())
    var userPlants = _userPlants.asStateFlow()
        private set

    fun getUserPlants(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        _isLoading.value = true
        try {
            userPlantRepository.getUserPlants {
                when (it) {
                    is Resource.Success -> {
                        _userPlants.value = it.data ?: emptyList()
                        _error.value = ""
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        onError(it.message ?: "An unexpected error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun createUserPlant(
        plantId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        _isLoading.value = true
        try {
            userPlantRepository.createUserPlant(plantId) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        onError(it.message ?: "An unexpected error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun deleteUserPlant(
        plantId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        _isLoading.value = true
        try {
            userPlantRepository.deleteUserPlant(plantId) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        onError(it.message ?: "An unexpected error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }
}