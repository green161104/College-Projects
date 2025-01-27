package ipp.estg.mobile.ui.screens.profile


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.mobile.ui.components.cards.ProfileInformation
import ipp.estg.mobile.ui.components.navigation.CustomTopAppBar
import ipp.estg.mobile.ui.components.utils.LightSquaredButton
import ipp.estg.mobile.ui.components.utils.Loading
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.AuthViewModel
import ipp.estg.mobile.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val user = userViewModel.user.collectAsState().value
    val isLoadingProfile = userViewModel.isLoading.collectAsState().value
    val error = userViewModel.error.collectAsState().value


    LaunchedEffect(Unit) {
        userViewModel.getUserInfo()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = "Profile",
                onBackClick = {
                    navController.popBackStack()
                    navController.navigate(Screen.Main.route)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            if (isLoadingProfile) {
                Loading()
            }

            if (user != null) {
                ProfileContent(
                    user = user,
                    error = error,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun ProfileContent(
    user: ipp.estg.mobile.data.retrofit.models.user.UserResponse,
    error: String,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {

        ProfileInformation(user = user)

        if (error.isNotEmpty()) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Button group with consistent styling
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(vertical = 10.dp)
        ) {
            LightSquaredButton(
                text = "Edit Preferences",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                textModifier = Modifier,
                fontWeight = FontWeight.Normal,
                textSize = 20,
                onClick = {
                    navController.navigate(Screen.EditUserPreferences.route)
                }
            )

            LightSquaredButton(
                text = "Change Profile Picture",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                textModifier = Modifier,
                fontWeight = FontWeight.Normal,
                textSize = 20,
                onClick = {
                    navController.navigate(Screen.EditProfileImagePage.route)
                }
            )

            LightSquaredButton(
                text = "Change Password",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                textModifier = Modifier,
                fontWeight = FontWeight.Normal,
                textSize = 20,
                onClick = {
                    navController.navigate(Screen.EditPasswordPage.route)
                }
            )

            LightSquaredButton(
                text = "Logout",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                textModifier = Modifier,
                fontWeight = FontWeight.Bold,
                textSize = 20,
                textColor = Color.Red,
                onClick = {
                    authViewModel.logout(
                        onSuccess = {
                            navController.popBackStack()
                            navController.navigate(Screen.Start.route)
                        }
                    )
                }
            )
        }
    }
}


