package com.cmu.estg.cmu_geocaching.util.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long) : Flow<Location>
    fun removeLocationUpdates()

    class LocationException(message:String): Exception()
}