package ipp.estg.mobile.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ipp.estg.mobile.data.repositories.DiaryRepository
import ipp.estg.mobile.data.retrofit.LeaflingsApiInstance
import ipp.estg.mobile.data.retrofit.models.diary.DiaryRequest
import ipp.estg.mobile.data.retrofit.models.diary.DiaryResponse
import ipp.estg.mobile.data.retrofit.models.diaryLogs.LogRequest
import ipp.estg.mobile.data.retrofit.models.diaryLogs.LogResponse
import ipp.estg.mobile.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DiaryViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val diaryRepository = DiaryRepository(
        LeaflingsApiInstance.getLeaflingsApi(application),
    )

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _log = MutableStateFlow<LogResponse?>(null)
    val log = _log.asStateFlow()

    private val _diary = MutableStateFlow<DiaryResponse?>(null)
    val diary = _diary.asStateFlow()

    private val _logs = MutableStateFlow<List<LogResponse>>(emptyList())
    val logs = _logs.asStateFlow()


    fun getDiary(
        userPlantId: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        try {
            diaryRepository.getDiary(userPlantId) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _diary.value = it.data
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        onError(it.message ?: "An error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun addDiary(
        diary: DiaryRequest,
        onSuccess: (addedDiary: DiaryResponse) -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) {

        _isLoading.value = true
        try {
            diaryRepository.addDiary(diary) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _isLoading.value = false
                        it.data?.let { it1 -> onSuccess(it1) }
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        onError(it.message ?: "An error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun getLogs(
        diaryId: Int,
        onSuccess: () -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) {

        _isLoading.value = true
        try {
            diaryRepository.getLogs(diaryId) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _logs.value = it.data ?: emptyList()
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        _logs.value = emptyList()
                        onError(it.message ?: "An error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun getLog(
        logId: Int,
        onSuccess: (fetchedLog: LogResponse) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        try {
            diaryRepository.getLog(logId) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _log.value = it.data
                        _isLoading.value = false
                        it.data?.let { it1 -> onSuccess(it1) }
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        onError(it.message ?: "An error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun addLog(
        diaryId: Int,
        description: String,
        onSuccess: () -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) {
        val log = LogRequest(diaryId, description)

        _isLoading.value = true
        try {
            diaryRepository.addLog(log) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        onError(it.message ?: "An error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun updateLog(
        logId: Int,
        diaryId: Int,
        newDescription: String,
        onSuccess: () -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) {
        _isLoading.value = true

        val newLog = LogRequest(diaryId, newDescription)
        try {
            diaryRepository.updateLog(logId, newLog) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        onError(it.message ?: "An error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }

    fun deleteLog(
        logId: Int,
        onSuccess: () -> Unit = {},
        onError: (message: String) -> Unit
    ) {
        _isLoading.value = true
        try {
            diaryRepository.deleteLog(logId) {
                when (it) {
                    is Resource.Success -> {
                        _error.value = ""
                        _isLoading.value = false
                        onSuccess()
                    }

                    is Resource.Error -> {
                        _error.value = "${it.message}"
                        _isLoading.value = false
                        onError(it.message ?: "An error occurred")
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
            _isLoading.value = false
        }
    }
}