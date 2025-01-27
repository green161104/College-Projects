package com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cmu.estg.cmu_geocaching.R
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.MapContentScreen
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.dropdownfielduseful
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.sharedNavBar
import com.cmu.estg.cmu_geocaching.util.location.LocationRepository
import com.cmu.estg.cmu_geocaching.util.location.LocationRepositoryProvider
import com.cmu.estg.cmu_geocaching.viewModel.SharedCacheViewModel
import com.mapbox.maps.MapView


@Preview(showSystemUi = true, device = "spec:width=320dp,height=568dp")
@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Preview(showBackground = true)
@Composable
fun FindCacheScreenPreview() {
    val navController = rememberNavController()
    val cache = Cache(
        cacheId = "1",
        createdBy = "1",
        difficulty = "Easy",
        latitude = "76.3827",
        longitude = "77.03921",
        rating = "1",
        image = "",
        description = "cache"
    )
    val sharedCacheViewModel: SharedCacheViewModel = viewModel()
    sharedCacheViewModel.setSelectedCache(cache)
    val locationRepository = LocationRepositoryProvider.repository
    FindCacheScreen(navController, sharedCacheViewModel, locationRepository)
}

@Composable
fun FindCacheScreen(
    navController: NavController,
    sharedCacheViewModel: SharedCacheViewModel,
    locationRepository: LocationRepository
) {
    Scaffold(
        bottomBar = {
            sharedNavBar(navController)
        }
    ) { innerPadding ->
        FindCacheDetailScreen(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            sharedCacheViewModel = sharedCacheViewModel,
            locationRepository = locationRepository
        )
    }
}

@Composable
fun FindCacheDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    sharedCacheViewModel: SharedCacheViewModel,
    locationRepository: LocationRepository
) {
    val configuration = LocalConfiguration.current
    var selectedRadius by remember { mutableStateOf<String?>(null) }
    val optionsRadius = listOf("5000 M", "10000 M", "25000 M")  // Changed to meters
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    val optionsDifficulty = listOf("Easy", "Medium", "Hard")

    val screenWidth = configuration.screenWidthDp
    val textSize = if (screenWidth < 360) 12.sp else 16.sp

    var showDialog by remember { mutableStateOf(false) }

    val allCaches by sharedCacheViewModel.allCaches.observeAsState()
    val context = LocalContext.current
    var mapView by remember { mutableStateOf<MapView?>(null) }

    var tempRadius by remember { mutableStateOf(selectedRadius ?: "No filter selected.") }
    var tempDifficulty by remember { mutableStateOf(selectedDifficulty ?: "No filter selected.") }

    val selectedCache by sharedCacheViewModel.selectedCache.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Map section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(configuration.screenHeightDp.dp * 0.4f)
                .background(Color.Magenta)
        ) {
            allCaches?.let {
                MapContentScreen(
                    caches = it,
                    locationRepository = locationRepository,
                    radius = selectedRadius?.removeSuffix(" M")?.toDoubleOrNull(),
                    difficulty = selectedDifficulty,
                    onMapViewReady = { providedMapView -> mapView = providedMapView },
                    onMarkerClick = { cache ->
                        showMarkerPopup(mapView!!, cache, context)
                        sharedCacheViewModel.setSelectedCache(cache)
                    }
                )
            }
        }

        // Filters and buttons section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.find_cache),
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            // Filters Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    dropdownfielduseful(
                        label = stringResource(R.string.radius),
                        currentValue = tempRadius,
                        options = optionsRadius,
                        onOptionSelected = { tempRadius = it }
                    )
                    Spacer(Modifier.height(8.dp))
                    dropdownfielduseful(
                        label = stringResource(R.string.difficulty),
                        currentValue = tempDifficulty,
                        options = optionsDifficulty,
                        onOptionSelected = { tempDifficulty = it }
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { showDialog = true },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info Icon",
                            tint = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(if (screenWidth < 360) 32.dp else 48.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            selectedRadius = null
                            selectedDifficulty = null
                            tempRadius = "No filter selected."
                            tempDifficulty = "No filter selected."
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear Filters Icon",
                            tint = Color(0xFFC45248),
                            modifier = Modifier.size(if (screenWidth < 360) 32.dp else 48.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    selectedRadius = tempRadius
                    selectedDifficulty = tempDifficulty
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF7CA68F)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.search_on_map),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = { navController.navigate("cacheinformation") },
                    colors = if (selectedCache != null) {
                        ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6456)) // Enabled state color
                    } else {
                        ButtonDefaults.buttonColors(containerColor = Color.Gray) // Disabled state color
                    },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .wrapContentSize()
                        .height(48.dp)
                        .border(3.dp, Color(0xFFB195DC), RoundedCornerShape(24.dp))
                        .shadow(2.dp, shape = RoundedCornerShape(24.dp)),
                    enabled = selectedCache != null
                ) {
                    Text(
                        text = stringResource(R.string.check_out_this_location),
                        color = if (selectedCache != null) Color.White else Color.LightGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }


    }

    // Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text(text = stringResource(R.string.hint),
                color = MaterialTheme.colorScheme.onBackground) },
            text = {
                Text(
                    text = stringResource(R.string.you_can_also_select_one_of_the_markers_on_the_map),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}


private fun showMarkerPopup(mapView: MapView, cache: Cache, context: Context) {
    val starRating = "★".repeat(minOf(cache.rating, 5)) + "☆".repeat(5 - minOf(cache.rating ?: 0, 5))

    android.app.AlertDialog.Builder(mapView.context)
        .setTitle("Cache Details")
        .setMessage(
            "Difficulty: ${cache.difficulty}\n" +
                    "Rating: $starRating\n" +
                    "Location: (${cache.latitude}, ${cache.longitude})"
        )
        .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
        .show()
    val toast = Toast.makeText(context, "Cache selected!", Toast.LENGTH_SHORT)
    toast.show()
}