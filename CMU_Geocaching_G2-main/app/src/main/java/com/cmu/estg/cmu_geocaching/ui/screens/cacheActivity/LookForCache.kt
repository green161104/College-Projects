package com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cmu.estg.cmu_geocaching.R
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.AppButton
import com.cmu.estg.cmu_geocaching.util.location.LocationRepository
import com.cmu.estg.cmu_geocaching.util.location.LocationRepositoryProvider
import com.cmu.estg.cmu_geocaching.viewModel.LocationViewModel
import com.cmu.estg.cmu_geocaching.viewModel.LocationViewModelFactory
import com.cmu.estg.cmu_geocaching.viewModel.SharedCacheViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


@Preview(showBackground = true)
@Composable
fun GeocachingScreenPreview() {
    val navController = rememberNavController()
    val cache = Cache(
        cacheId = "1",
        createdBy = "njfnjksnfe",
        longitude = "-122.4194",
        latitude = "37.7749",
        difficulty = "Easy",
        image = "",
        rating = 1,
        description = ""
    )
    val context = LocalContext.current.applicationContext as Application
    val sharedCacheViewModel: SharedCacheViewModel = viewModel()
    val locationRepository: LocationRepository = LocationRepositoryProvider.repository
    sharedCacheViewModel.setSelectedCache(cache)
    GeocachingScreen(navController, context, sharedCacheViewModel, locationRepository)
}

@Composable
fun GeocachingScreen(
    navController: NavController,
    application: Application,
    sharedViewModel: SharedCacheViewModel,
    locationRepository: LocationRepository
) {

    val cache = sharedViewModel.selectedCache.collectAsState().value
    if (cache == null) {
        Toast.makeText(application, stringResource(R.string.no_cache_selected), Toast.LENGTH_SHORT).show()
        LaunchedEffect(Unit) {
            navController.navigate("cacheinformation")
        }
        // Stop further execution
        return
    }
    // Create location client and factory
    val factory = LocationViewModelFactory(application, locationRepository, cache)

    // Get the LocationViewModel
    val locationViewModel: LocationViewModel = viewModel(factory = factory)


    val isClose by locationViewModel.isCloseToObjective.collectAsState()

    // Adjust button based on proximity
    val buttonText =
        if (isClose) stringResource(R.string.complete_objective) else stringResource(R.string.end_caching)
    val onClickAction: () -> Unit = {
        if (isClose) {
            navController.navigate("triviaquestion")
        } else {
            navController.navigate("findcache")
        }
    }


    var timePassed by remember { mutableStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        while (isActive) {
            delay(1000L)
            timePassed++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.youre_looking_for),
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = cache.description,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppButton(onClickAction = {
            openGoogleMaps(context, cache.latitude, cache.longitude, "Cache Location")
        }, "Open Google Maps")

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = String.format(
                stringResource(R.string.time_passed) + ": %02d:%02d:%02d",
                timePassed / 3600,
                (timePassed % 3600) / 60,
                timePassed % 60
            ),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppButton(onClickAction = onClickAction, buttonText)
    }
}


private fun openGoogleMaps(context: Context, latitude: String, longitude: String, label: String = "Marker") {
    val gmmIntentUri = Uri.parse("geo:0,0?q=${latitude},${longitude}(${Uri.encode(label)})")

    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

    // Make the Intent explicit by setting the Google Maps package
    mapIntent.setPackage("com.google.android.apps.maps")

    // Check if Google Maps is installed
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        Toast.makeText(
            context,
            context.getString(R.string.google_maps_is_not_installed), Toast.LENGTH_SHORT
        ).show()
    }
}



