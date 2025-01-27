package ipp.estg.mobile.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ipp.estg.mobile.data.preferences.AuthPreferences
import ipp.estg.mobile.data.repositories.PlantRepository
import ipp.estg.mobile.data.retrofit.LeaflingsApiInstance
import ipp.estg.mobile.data.retrofit.models.plant.PlantResponse
import ipp.estg.mobile.data.retrofit.models.task.TaskResponse
import ipp.estg.mobile.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlantViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val plantRepository = PlantRepository(
        LeaflingsApiInstance.getLeaflingsApi(application)
    )
    private val authPreferences = AuthPreferences(application)

    // Propriedade para acessar o rolePaid
    val userRolePaid: Boolean get() = authPreferences.getRolePaid()


    private val _plants = MutableStateFlow<List<PlantResponse>>(emptyList())
    var plants = _plants.asStateFlow()
        private set

    private val _plant = MutableStateFlow<PlantResponse?>(null)
    var plant = _plant.asStateFlow()
        private set

    private val _tasks = MutableStateFlow<List<TaskResponse>>(emptyList())
    var tasks = _tasks.asStateFlow()
        private set


    private val _isLoading = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()
        private set

    private val _error = MutableStateFlow("")
    var error = _error.asStateFlow()
        private set



    fun getPlants(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        try {
            plantRepository.getPlants {
                when (it) {
                    is Resource.Success -> {
                        _plants.value = it.data?.data ?: emptyList()
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

    fun getPlantById(
        id: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        try {
            plantRepository.getPlantById(id) {
                when (it) {
                    is Resource.Success -> {
                        _plant.value = it.data
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

    fun getPlantTasks(
        plantId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        try {
            plantRepository.getPlantTasks(plantId) {
                when (it) {
                    is Resource.Success -> {
                        _tasks.value = it.data?.data ?: emptyList()
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