package com.cmu.estg.cmu_geocaching.viewModel

import android.app.Application
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewModelScope
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.util.NotificationHelper.notifyUser
import com.cmu.estg.cmu_geocaching.util.location.LocationClient
import com.cmu.estg.cmu_geocaching.util.location.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class LocationViewModel(
    application: Application,
    private val locationRepository: LocationRepository,
    val cache: Cache
) : AndroidViewModel(application) {

    private val _locationUpdates = MutableStateFlow<Location?>(null)

    private val _isCloseToObjective = MutableStateFlow(false)
    val isCloseToObjective: StateFlow<Boolean> = _isCloseToObjective

    private val cacheLocation = Location("").apply {
        latitude = cache.latitude.toDouble()
        longitude = cache.longitude.toDouble()
    }

    private var wasClose = false

    init {
        startLocationTracking()
    }

    fun startLocationTracking() {
        viewModelScope.launch {
            locationRepository.location
                .onEach { location ->
                    _locationUpdates.value = location
                    location?.let {
                        updateProximityStatus(it)
                    }
                    Log.d("location", location.toString())
                }
                .catch { e ->
                    Toast.makeText(
                        getApplication(),
                        "Failed to fetch location updates: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .launchIn(viewModelScope)
        }
    }


    private fun checkDistance(currentLocation: Location): Boolean {
        return currentLocation.distanceTo(cacheLocation) <= 10 //less than 10 meters away
        //CHANGE DISTANCE VALUE TO BE HIGHER FOR TESTING IF NEEDED:)
    }

    private fun updateProximityStatus(currentLocation: Location) {
        val isClose = checkDistance(currentLocation)
        Log.d("Current distance", checkDistance(currentLocation).toString())

        _isCloseToObjective.value = isClose

        if (isClose && !wasClose) {
            notifyUser(getApplication(), "You're close!", "You're within 10 meters of the cache!")
            wasClose = true
        } else if (!isClose) {
            wasClose = false
        }
    }


}

class LocationViewModelFactory(
    private val application: Application,
    private val locationRepository: LocationRepository,
    private val cache: Cache
) : Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return LocationViewModel(application, locationRepository, cache) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
