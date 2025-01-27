package com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.cmu.estg.cmu_geocaching.R
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.util.location.LocationRepository
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener

@SuppressLint("RememberReturnType")
@Composable
fun MapContentScreen(
    caches: List<Cache>,
    locationRepository: LocationRepository,
    difficulty: String? = null,
    radius: Double? = null,
    onMarkerClick: (Cache) -> Unit,
    onMapViewReady: (MapView) -> Unit
) {
    val context = LocalContext.current

    val currentLocation by locationRepository.location.collectAsState()

    val defaultPoint = Point.fromLngLat(-8.27, 41.45)



    val mapView = remember {
        MapView(context).apply {
            val locationPoint = currentLocation?.let { location ->
                Point.fromLngLat(location.longitude, location.latitude)
            } ?: defaultPoint

            // Initial camera setup
            mapboxMap.setCamera(
                CameraOptions.Builder()
                    .zoom(6.0)
                    .center(locationPoint)
                    .pitch(0.0)
                    .bearing(0.0)
                    .build()
            )
            mapboxMap.loadStyle(Style.MAPBOX_STREETS)
        }
    }

    LaunchedEffect(mapView) {
        onMapViewReady(mapView)
    }

    // Manage lifecycle and cleanup
    DisposableEffect(mapView) {
        onDispose {
            mapView.onStop()
            mapView.onDestroy()
        }
    }
    val annotationManager = remember { mapView.annotations.createPointAnnotationManager() }
    val nocachetext = stringResource(id = R.string.no_caches_found_criteria)

    // Add caches as markers
    LaunchedEffect(caches, difficulty, radius) {
        annotationManager.deleteAll()
        Log.d("MAP", annotationManager.annotations.isEmpty().toString())
        val markerBitmap = createMarkerBitmap(context, R.drawable.baseline_place_24)

        val locationPoint = currentLocation?.let { location ->
            Point.fromLngLat(location.longitude, location.latitude)
        }

        val filteredCaches = caches.filter { cache ->
            val difficultyMatch = difficulty == null || cache.difficulty == difficulty || difficulty == "No filter selected."
            val radiusMatch =
                radius == null || locationPoint == null || calculateDistanceBetweenPoints(
                    Point.fromLngLat(cache.longitude.toDouble(), cache.latitude.toDouble()),
                    locationPoint
                ) <= radius

            difficultyMatch && radiusMatch
        }

        if (filteredCaches.isEmpty()) {
            annotationManager.deleteAll()
            val toast = Toast.makeText(
                context,
                nocachetext,
                Toast.LENGTH_SHORT
            )
            toast.show()
            return@LaunchedEffect

        }else {
            val markers = mutableListOf<Pair<PointAnnotationOptions, Cache>>()

            filteredCaches.forEach { cache ->

                val cacheLocation =
                    Point.fromLngLat(cache.longitude.toDouble(), cache.latitude.toDouble())

                val pointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(cacheLocation)
                    .withIconImage(markerBitmap)
                    .withIconSize(2.0)

                try {
                    annotationManager.create(pointAnnotationOptions)
                    markers.add(Pair(pointAnnotationOptions, cache))
                } catch (e: Exception) {
                    Log.e("MapContentScreen", "Failed to create marker: ${e.message}")
                }
            }

            mapView.mapboxMap.addOnMapClickListener { point ->
                val closestMarker = markers.minByOrNull { (markerOptions, _) ->
                    val markerPoint = markerOptions.getPoint()!!
                    calculateDistanceBetweenPoints(point, markerPoint)
                }

                // Define a reasonable threshold distance (in meters)
                val MAX_DISTANCE_THRESHOLD = 50000.0

                // Show popup for the closest marker if within the threshold distance
                closestMarker?.let { (markerOptions, cache) ->
                    val distance = calculateDistanceBetweenPoints(point, markerOptions.getPoint()!!)
                    if (distance <= MAX_DISTANCE_THRESHOLD) {
                        onMarkerClick(cache)
                    }
                }

                true // Consume the event
            }
        }
    }


// Display the MapView
    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}

private fun calculateDistanceBetweenPoints(point1: Point, point2: Point): Float {
    Log.d("DISTANCE FILTER", "Point1: Lat=${point1.latitude()}, Lon=${point1.longitude()}")
    Log.d("DISTANCE FILTER", "Point2: Lat=${point2.latitude()}, Lon=${point2.longitude()}")

    val results = FloatArray(1)
    Location.distanceBetween(
        point1.latitude(), point1.longitude(),
        point2.latitude(), point2.longitude(),
        results
    )
    Log.d("DISTANCE FILTER", "Distance: ${results[0]} meters")
    return results[0]
}


private fun createMarkerBitmap(context: Context, @DrawableRes drawableResId: Int): Bitmap {
    // Use ContextCompat to get the drawable
    val drawable = ContextCompat.getDrawable(context, drawableResId)
    if (drawable == null) {
        Log.e("MapContentScreen", "Drawable resource not found: $drawableResId")
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Return a dummy bitmap
    }

    // Create a bitmap drawable with intrinsic width and height
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}