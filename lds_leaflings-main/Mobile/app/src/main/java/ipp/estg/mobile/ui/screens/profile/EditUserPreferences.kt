package ipp.estg.mobile.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.mobile.data.retrofit.models.user.UserResponse
import ipp.estg.mobile.ui.components.forms.PreferenccesForm
import ipp.estg.mobile.ui.components.navigation.CustomTopAppBar
import ipp.estg.mobile.ui.components.utils.Loading
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserPreferencesPage(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {

    val context = LocalContext.current
    val user = userViewModel.user.collectAsState().value
    val isLoading = userViewModel.isLoading.collectAsState().value
    val error = userViewModel.error.collectAsState().value

    LaunchedEffect(Unit) {
        userViewModel.getUserInfo()
    }

    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = "My Preferences",
                onBackClick = {
                    navController.popBackStack()
                    navController.navigate(Screen.ProfileScreen.route)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 20.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if(user != null) {
                Form(
                    navController = navController,
                    userViewModel = userViewModel,
                    user = user
                )
            }

            if (isLoading) {
                Loading()
            }
        }
    }
}

@Composable
private fun Form(
    navController: NavController,
    userViewModel: UserViewModel,
    user: UserResponse
) {


    var waterAvailability by remember {
        mutableStateOf(
            user.waterAvailability
        )
    }

    var careExperience by remember {
        mutableStateOf(
            user.careExperience
        )
    }

    var luminosityAvailability by remember {
        mutableStateOf(
            user.luminosityAvailability
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        PreferenccesForm(
            buttonText = "Edit Preferences",
            waterAvailability = waterAvailability,
            careExperience = careExperience,
            luminosityAvailability = luminosityAvailability,
            onWaterAvailabilitySelected = { waterAvailability = it },
            onCareExperienceSelected = { careExperience = it },
            onLuminosityAvailabilitySelected = { luminosityAvailability = it },
            onButtonClicked = {
                userViewModel.updateUserPreferences(
                    waterAvailability = waterAvailability,
                    careExperience = careExperience,
                    luminosityAvailability = luminosityAvailability,
                    onSuccess = {
                        navController.popBackStack()
                        navController.navigate(Screen.ProfileScreen.route)
                    }
                )
            }
        )
    }
}