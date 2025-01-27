package com.cmu.estg.cmu_geocaching.ui.screens.registerscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.InputFieldAuth
import com.cmu.estg.cmu_geocaching.ui.theme.LightColorScheme
import com.cmu.estg.cmu_geocaching.viewModel.AuthViewModel
import com.cmu.estg.cmu_geocaching.viewModel.HomePageViewModel


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    val homePageViewModel = viewModel<HomePageViewModel>()

    LoginScreen(navController)
}
@Composable
fun LoginScreen(navController: NavController) {
    MaterialTheme (
        colorScheme = LightColorScheme
    ){
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp

        // Use rememberSaveable for persistent state across recompositions
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

    val authViewModel: AuthViewModel = viewModel()
    val authStatus by authViewModel.authState.observeAsState(AuthViewModel.AuthStatus.NOLOGGIN)
    val context = LocalContext.current

    val errorMessage by authViewModel.errorMessage.collectAsState()

        // Box containing the Login Screen UI
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFA8E6CF),
                            Color(0xFFF4FAEE)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (screenWidth > 600) 48.dp else 24.dp)
                    .verticalScroll(rememberScrollState()), // Add scrolling for smaller screens
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome Text
                Text(
                    text = stringResource(R.string.welcome_back),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = (if (screenWidth > 600) 36 else 32).sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF74558B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.we_missed_you),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = (if (screenWidth > 600) 18 else 16).sp
                    ),
                    color = Color(0xFF74558B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email Input
                InputFieldAuth(
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.email_input),
                    isPassword = false,
                    value = email,
                    onValueChange = { email = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Input
                InputFieldAuth(
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.password_input),
                    isPassword = true,
                    value = password,
                    onValueChange = { password = it }
                )

            Spacer(modifier = Modifier.height(8.dp))

            // Show login error message inline
            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
                val fillfieldsmessage = stringResource(R.string.fill_fields)
            Button(
                onClick = {
                    // Basic validation
                    if (email.isNotBlank() && password.isNotBlank()) {
                        authViewModel.login(email, password)
                    } else {
                        Toast.makeText(context, fillfieldsmessage, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(if (screenWidth > 600) 0.6f else 0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D6FAC))
            ) {
                Text(
                    text = stringResource(R.string.login)   ,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

                Spacer(modifier = Modifier.height(16.dp))

                // Optional: Register link
                TextButton(
                    onClick = {
                        // Navigate to registration screen
                        navController.navigate("register")
                    }
                ) {
                    Text(
                        text = stringResource(R.string.no_account),
                        color = Color(0xFF74558B),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Handle navigation after successful login
            LaunchedEffect(authStatus) {
                if (authStatus == AuthViewModel.AuthStatus.LOGGED) {
                    navController.navigate("homepage") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }
}