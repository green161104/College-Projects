package ipp.estg.mobile.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ipp.estg.mobile.data.repositories.AdRepository
import ipp.estg.mobile.data.retrofit.LeaflingsApiInstance
import ipp.estg.mobile.data.retrofit.models.ad.AdResponse
import ipp.estg.mobile.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdViewModel(
    application: Application
): AndroidViewModel(application) {

    private val adRepository = AdRepository(
        LeaflingsApiInstance.getLeaflingsApi(application)
    )

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _ad = MutableStateFlow<AdResponse?>(null)
    val ad = _ad

    fun getRandomAd(
        onSuccess: () -> Unit = {}
    ){
        _isLoading.value = true
        adRepository.getRandomAd {
            when(it){
                is Resource.Success -> {
                    _error.value = ""
                    _ad.value = it.data
                    _isLoading.value = false
                    onSuccess()
                }
                is Resource.Error -> {
                    _error.value = "${it.message}"
                    _isLoading.value = false
                }
            }
        }
    }
}