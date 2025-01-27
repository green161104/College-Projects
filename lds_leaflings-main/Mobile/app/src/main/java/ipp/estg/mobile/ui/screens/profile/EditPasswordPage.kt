package ipp.estg.mobile.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.mobile.ui.components.forms.PasswordForm
import ipp.estg.mobile.ui.components.navigation.CustomTopAppBar
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordPage(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {

    val isLoading = userViewModel.isLoading.collectAsState().value
    val error = userViewModel.error.collectAsState().value

    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = "Change Password",
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
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var oldPassword by remember { mutableStateOf("") }
            var newPassword by remember { mutableStateOf("") }

            PasswordForm(
                oldPassword = oldPassword,
                newPassword = newPassword,
                onOldPasswordChange = { oldPassword = it },
                onNewPasswordChange = { newPassword = it },
                onSubmit = {
                    userViewModel.updatePassword(
                        oldPassword = oldPassword,
                        newPassword = newPassword,
                        onSuccess = {
                            navController.navigate(Screen.ProfileScreen.route)
                        }
                    )
                }
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}