package ipp.estg.mobile.ui.screens.profile

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ipp.estg.mobile.ui.components.forms.ImageForm
import ipp.estg.mobile.ui.components.navigation.CustomTopAppBar
import ipp.estg.mobile.ui.components.utils.Loading
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EditProfileImagePage(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val isLoading = userViewModel.isLoading.collectAsState().value
    val error = userViewModel.error.collectAsState().value

    // Define a permissão adequada com base na versão do Android
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // Solicita a permissão
    val galleryPermission = rememberPermissionState(permission)

    LaunchedEffect(galleryPermission.status) {
        if (!galleryPermission.status.isGranted) {
            galleryPermission.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = "Change Image",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!galleryPermission.status.isGranted) {
                Text("Gallery permission not granted", color = MaterialTheme.colorScheme.error)
            } else {
                var selectedImageUri by remember { mutableStateOf<String?>("") }

                ImageForm(
                    selectedImageUri = selectedImageUri,
                    onImageSelected = { uri ->
                        selectedImageUri = uri
                    },
                    onUpdateClick = {
                        selectedImageUri?.let { uri ->
                            userViewModel.updateImage(
                                imageUri = uri,
                                onSuccess = {
                                    navController.popBackStack()
                                    navController.navigate(Screen.ProfileScreen.route)
                                }
                            )
                        }
                    }
                )
            }

            if(isLoading) {
                Loading()
            }

            if (error.isNotEmpty()) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}