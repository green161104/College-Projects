package com.cmu.estg.cmu_geocaching

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cmu.estg.cmu_geocaching.ui.theme.CMU_GeocachingTheme
import com.cmu.estg.cmu_geocaching.util.LanguageContextWrapper
import com.cmu.estg.cmu_geocaching.util.location.LocationRepositoryProvider
import com.cmu.estg.cmu_geocaching.util.location.LocationService
import com.cmu.estg.cmu_geocaching.viewModel.SettingsViewModel

class MainActivity : ComponentActivity() {
    private val locationRepository = LocationRepositoryProvider.repository
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkMode = settingsViewModel.isDarkMode.collectAsState()
            val selectedLanguage = settingsViewModel.selectedLanguage.collectAsState()
            LanguageContextWrapper.wrap(this, selectedLanguage.value)
            CMU_GeocachingTheme(darkTheme = isDarkMode.value) {
                Scaffold(modifier = Modifier.fillMaxSize()) {

                    Nav(locationRepository, settingsViewModel)
                }
            }
            requestPermissions()
        }

    }

    @SuppressLint("InlinedApi")
    private fun requestPermissions() {
        // Request the necessary permissions
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.POST_NOTIFICATIONS
        )

        val missingPermissions = permissions.filter {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                missingPermissions.toTypedArray(),
                0
            )
        } else {
            startLocationService() // Start the service only if permissions are granted
            setupNotificationChannels()
        }
    }

    private fun startLocationService() {
        // In MainActivity or wherever the service is started
        val serviceIntent = Intent(this, LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        LocationService.setLocationRepository(locationRepository)
        startService(serviceIntent)
    }

    private fun setupNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location", // Channel ID
                "Location Notifications", // Channel name
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for location-based events"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
            Log.d("App", "Notifications enabled.")
        }
    }



    override fun onDestroy() {
        super.onDestroy()
    }


}


/*
remember stores objects in the Composition,
and forgets the object if the source location
where remember is called is not invoked again during a recomposition!
 */
