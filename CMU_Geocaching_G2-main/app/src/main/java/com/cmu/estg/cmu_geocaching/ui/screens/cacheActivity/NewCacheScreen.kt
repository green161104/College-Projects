package com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.cmu.estg.cmu_geocaching.data.local.GeocachingDatabase
import com.cmu.estg.cmu_geocaching.data.local.repository.CacheRepository
import com.cmu.estg.cmu_geocaching.data.local.repository.UserRepository
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.AppButton
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.InputField
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.dropdownfielduseful
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.sharedNavBar
import com.cmu.estg.cmu_geocaching.viewModel.CacheCreationViewModel
import com.cmu.estg.cmu_geocaching.viewModel.CacheCreationViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


@Preview(showSystemUi = true, device = "spec:width=320dp,height=568dp")
@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun NewCacheScreenPreview() {
    val navController = rememberNavController()
    NewCacheScreen(navController)
}

@Composable
fun NewCacheScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val cacheDao = GeocachingDatabase.getDatabase(context).cacheDao()
    val userDao = GeocachingDatabase.getDatabase(context).userDao()
    val firestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val cacheRepository = CacheRepository(cacheDao, firestore)
    val userRepository = UserRepository(firebaseAuth, firestore, userDao)

    val viewModelFactry =
        CacheCreationViewModelFactory(application, cacheRepository, userRepository)
    val cacheCreationViewModel: CacheCreationViewModel = viewModel(factory = viewModelFactry)

    Scaffold(bottomBar = {
        sharedNavBar(navController)
    }) { innerPadding -> // innerPadding passed here
        NewCache(
            Modifier.padding(innerPadding), navController, cacheCreationViewModel
        ) // Pass the padding to the NewCache composable
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCache(
    modifier: Modifier, navController: NavController, cacheCreationViewModel: CacheCreationViewModel
) {
    var longitude by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf("Easy") }

    val optionsDifficulty = listOf(
        stringResource(R.string.easy),
        stringResource(R.string.medium),
        stringResource(R.string.hard)
    )

    val scrollState = rememberScrollState()
    val errorMessage by cacheCreationViewModel.errorMessage.collectAsState()
    val cacheCreationSuccess by cacheCreationViewModel.cacheCreationSuccess.collectAsState()

    if (errorMessage != null) {
        Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
    }
    LaunchedEffect(key1 = cacheCreationSuccess) {
        if (cacheCreationSuccess) {
            navController.navigate("homepage")
        }
    }

    Column(
        modifier = modifier // Use the modifier with padding here
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back button and Title
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) { // Go back to the previous screen
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = stringResource(R.string.new_cache),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Latitude Input
        InputField(
            label = "Latitude",
            placeholder = "Latitude",
            onValueChange = { latitude = it },
            value = latitude
        )

        // Longitude Input
        InputField(
            label = "Longitude",
            placeholder = "Longitude",
            onValueChange = { longitude = it },
            value = longitude
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Difficulty Input
        dropdownfielduseful(label = stringResource(R.string.difficulty),
            currentValue = selectedDifficulty,
            options = optionsDifficulty,
            onOptionSelected = { selectedDifficulty = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Description Input
        TextField(value = description,
            onValueChange = { description = it },
            placeholder = {
                Text(
                    text = stringResource(R.string.helpful_desc_placeholder),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Cost and Create button
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.cost_points),
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(onClickAction = {
                val uniqueId = UUID.randomUUID().toString()
                cacheCreationViewModel.createCache(
                    cacheId = uniqueId,
                    difficulty = selectedDifficulty,
                    latitude = latitude,
                    longitude = longitude,
                    description = description
                )
            }, buttonText = stringResource(R.string.create_cache))

        }
    }
}