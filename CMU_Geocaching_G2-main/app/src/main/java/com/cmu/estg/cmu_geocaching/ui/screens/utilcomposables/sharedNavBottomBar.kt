package com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun sharedNavBar(navController: NavController) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val textSize = if (screenWidth < 360) 8.sp else 12.sp

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "History", tint = MaterialTheme.colorScheme.onPrimary) },
            label = { Text(stringResource(id = com.cmu.estg.cmu_geocaching.R.string.history), fontSize = textSize, color = MaterialTheme.colorScheme.onPrimary) },
            selected = false,
            onClick = { navController.navigate("cachehistory") { popUpTo("homepage") } },
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Homepage", tint = MaterialTheme.colorScheme.onPrimary) },
            label = { Text(stringResource(id = com.cmu.estg.cmu_geocaching.R.string.homepage), fontSize = textSize, color = MaterialTheme.colorScheme.onPrimary) },
            selected = false,
            onClick = { navController.navigate("homepage") { popUpTo("homepage") } }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Default.Face, contentDescription = "Profile ", tint = MaterialTheme.colorScheme.onPrimary) },
            label = { Text(stringResource(id = com.cmu.estg.cmu_geocaching.R.string.profile), fontSize = textSize, color = MaterialTheme.colorScheme.onPrimary) },
            selected = false,
            onClick = { navController.navigate("profile") { popUpTo("homepage") } } // go to profile
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onPrimary) },
            label = { Text(stringResource(id = com.cmu.estg.cmu_geocaching.R.string.settings), fontSize = textSize, color = MaterialTheme.colorScheme.onPrimary) },
            selected = false,
            onClick = { navController.navigate("settings") { popUpTo("homepage") } }
        )
    }
}