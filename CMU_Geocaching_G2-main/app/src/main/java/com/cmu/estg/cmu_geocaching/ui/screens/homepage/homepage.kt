package com.cmu.estg.cmu_geocaching.ui.screens.homepage


import android.app.AlertDialog
import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cmu.estg.cmu_geocaching.R
import com.cmu.estg.cmu_geocaching.data.local.GeocachingDatabase
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.data.local.repository.CacheRepository
import com.cmu.estg.cmu_geocaching.data.local.repository.UserRepository
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.ActionCard
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.MapContentScreen
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.SharedModalNavigationDrawer
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.sharedNavBar
import com.cmu.estg.cmu_geocaching.util.location.LocationRepository
import com.cmu.estg.cmu_geocaching.viewModel.HomePageViewModel
import com.cmu.estg.cmu_geocaching.viewModel.HomePageViewModelFactory
import com.cmu.estg.cmu_geocaching.viewModel.LightSensorViewModel
import com.cmu.estg.cmu_geocaching.viewModel.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mapbox.maps.MapView

@Composable
fun Homepage(
    navController: NavController,
    locationRepository: LocationRepository,
    settingsViewModel: SettingsViewModel,
    lightSensorViewModel: LightSensorViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val userDao = GeocachingDatabase.getDatabase(context).userDao()
    val cacheDao = GeocachingDatabase.getDatabase(context).cacheDao()
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val userRepository = UserRepository(firebaseAuth, firestore, userDao)
    val cacheRepository = CacheRepository(cacheDao, firestore)
    // ViewModel Factory
    val homePageViewModelFactory =
        HomePageViewModelFactory(application, userRepository, cacheRepository)

    // Create Homepage viewmodel
    val homePageViewModel: HomePageViewModel = viewModel(factory = homePageViewModelFactory)
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
    var showPrompt by remember { mutableStateOf(false) }
    val hasPromptBeenShown by homePageViewModel.hasPromptBeenShown.collectAsState()


    LightSensorHandler(
        lightSensorViewModel = lightSensorViewModel,
        isDarkMode = isDarkMode,
        onDarkModePrompt = { showPrompt = true }
    )

    SharedModalNavigationDrawer(navController, drawerState, scope) {
        if (showPrompt && !hasPromptBeenShown) {
            AlertDialog(
                onDismissRequest = { showPrompt = false; homePageViewModel.setPromptShown() },
                title = { Text(stringResource(id = R.string.switch_dark)) },
                text = { Text(stringResource(id = R.string.switch_dark_desc)) },
                confirmButton = {
                    Button(onClick = {
                        settingsViewModel.updateDarkMode(true)
                        showPrompt = false
                        homePageViewModel.setPromptShown()
                    }) {
                        Text(stringResource(id = R.string.yes))
                    }
                },
                dismissButton = {
                    Button(onClick = { showPrompt = false; homePageViewModel.setPromptShown()  }) {
                        Text(stringResource(id = R.string.no))
                    }
                }
            )
        }
        Scaffold(
            bottomBar = {
                sharedNavBar(navController)
            }
        ) { innerPadding ->

            homepagecontent(innerPadding, navController, homePageViewModel, locationRepository)
        }
    }
}


@Composable
fun homepagecontent(
    innerPadding: PaddingValues,
    navController: NavController,
    homePageViewModel: HomePageViewModel,
    locationRepository: LocationRepository
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val configuration = LocalConfiguration.current
    val smallDevicePadding = if (screenWidth < 360) 14.dp else 28.dp
    val isLandscape = remember { configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }
    val currentUser by homePageViewModel.currentUser.collectAsState()
    val allCaches by homePageViewModel.allCaches.observeAsState()
    var mapView by remember { mutableStateOf<MapView?>(null) }


    // Create a scroll state for scrollable content
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(if (isLandscape) scrollState else rememberScrollState())

    ) {
        // Map Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(configuration.screenHeightDp.dp * 0.4f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            allCaches?.let {
                val cachedetails = stringResource(id = R.string.cache_details)
                val difficulty = stringResource(id = R.string.difficulty)
                val rating = stringResource(id = R.string.rating)
                val location = stringResource(id = R.string.location)
                val close = stringResource(id = R.string.close)
                MapContentScreen(
                    caches = it,
                    locationRepository = locationRepository,
                    onMapViewReady = { providedMapView ->
                        mapView = providedMapView
                    },
                    onMarkerClick = { cache ->
                        mapView?.let { it1 -> showMarkerPopup(
                            it1,
                            cache,
                            cachedetails,
                            difficulty,
                            rating,
                            location,
                            close) }
                    })

            }
        }

        Spacer(modifier = Modifier.height(42.dp))

        // Greeting and Points Section
        Row(
            modifier = Modifier
                .padding(horizontal = smallDevicePadding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.hello_user) + " ${currentUser?.name}",
                    fontSize = if (screenWidth < 360) 20.sp else 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.ready_adventure),
                    fontSize = if (screenWidth < 360) 8.sp else 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${currentUser?.points}",
                    fontSize = if (screenWidth < 360) 20.sp else 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = stringResource(id = R.string.your_points), color = MaterialTheme.colorScheme.onBackground)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions Section
        ActionCard(
            title = stringResource(id = R.string.create_a_new_cache),
            points = stringResource(id = R.string.homepage_points),
            backgroundColor = MaterialTheme.colorScheme.surface,
            onClick = { navController.navigate("newcache") },
        )
        Spacer(modifier = Modifier.height(8.dp))
        ActionCard(
            title = stringResource(id = R.string.go_hunting),
            points = stringResource(id = R.string.earn_more_points),
            backgroundColor = MaterialTheme.colorScheme.surface,
            onClick = { navController.navigate("findcache") },
        )
    }

}

private fun showMarkerPopup(
    mapView: MapView,
    cache: Cache,
    cachedetails: String,
    difficulty: String,
    rating: String,
    location: String,
    close: String) {
    val starRating = "★".repeat(cache.rating ?: 0) + "☆".repeat(5 - (cache.rating ?: 0))

    AlertDialog.Builder(mapView.context)
        .setTitle(cachedetails)
        .setMessage(
            difficulty + ": ${cache.difficulty}\n" +
                    rating + ": $starRating\n" +
                    location + ": (${cache.latitude}, ${cache.longitude})"
        )
        .setPositiveButton(close) { dialog, _ -> dialog.dismiss() }
        .show()
}


@Composable
fun LightSensorHandler(
    lightSensorViewModel: LightSensorViewModel,
    isDarkMode: Boolean,
    onDarkModePrompt: () -> Unit
) {
    val lightLevel by lightSensorViewModel.lightLevel.collectAsState()

    LaunchedEffect(lightLevel) {
        if (lightLevel < 20 && !isDarkMode) {
            onDarkModePrompt()
        }
    }
}



