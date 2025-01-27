package ipp.estg.mobile.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.mobile.R
import ipp.estg.mobile.ui.components.forms.LoginForm
import ipp.estg.mobile.ui.components.utils.Loading
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.AuthViewModel

@Composable
fun LoginPage(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {

    val context = LocalContext.current
    val error = authViewModel.error.collectAsState().value
    val isLoading = authViewModel.isLoading.collectAsState().value

    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header()

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        LoginForm(
            email = email,
            password = password,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onLoginClick = {
                authViewModel.login(
                    email = email,
                    password = password,
                    onSuccess = {
                        navController.navigate(Screen.Main.route)
                    }
                )
            }
        )

        if (isLoading) {
            Loading()
        }


        Text(
            text = error, color = MaterialTheme.colorScheme.error,
            modifier = Modifier.testTag("error_message")
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun Header() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 60.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.leaflings_logo),
            contentDescription = "Leaflings Logo",
            modifier = Modifier
                .size(100.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Leaflings",
            fontSize = 55.sp,
            textAlign = TextAlign.Center
        )
    }
}