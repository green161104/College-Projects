package com.cmu.estg.cmu_geocaching.ui.screens.registerscreen

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.InputField
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.InputFieldAuth
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.dropdownfielduseful
import com.cmu.estg.cmu_geocaching.ui.theme.LightColorScheme
import com.cmu.estg.cmu_geocaching.viewModel.AuthViewModel
import com.cmu.estg.cmu_geocaching.viewModel.HomePageViewModel


@Composable
fun RegistrationScreen(navController: NavController) {
    MaterialTheme(
        colorScheme = LightColorScheme
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val screenHeight = configuration.screenHeightDp

        val horizontalPadding =
            if (screenWidth > 600) 48.dp else 32.dp // Adjust padding for larger screens
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var selectedPronoun by remember { mutableStateOf("they/them") }
        val options = listOf("he/him", "she/her", "they/them")
        var password by remember { mutableStateOf("") }
        val authViewModel: AuthViewModel = viewModel()
        val authStatus by authViewModel.authState.observeAsState(AuthViewModel.AuthStatus.NOLOGGIN) // Default to NOLOGGIN

        // Error messages
        var nameError by remember { mutableStateOf<String?>(null) }
        var emailError by remember { mutableStateOf<String?>(null) }
        var passwordError by remember { mutableStateOf<String?>(null) }

        val nameerror = stringResource(id = R.string.name_is_required)
        val emailerror = stringResource(id = R.string.email_error)
        val passworderror = stringResource(id = R.string.password_error)

        fun validateInputs(): Boolean {
            var isValid = true

            // Validate name
            if (name.isEmpty()) {
                nameError = nameerror
                isValid = false
            } else {
                nameError = null
            }

            // Validate email
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError = emailerror
                isValid = false
            } else {
                emailError = null
            }

            // Validate password
            if (password.length < 8) {
                passwordError = passworderror
                isValid = false
            } else {
                passwordError = null
            }

            return isValid
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF4FAEE),
                            Color(0xFFA8E6CF)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding, vertical = 16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()), // Add scrolling for smaller screens
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome text
                Text(
                    text = "Welcome!",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = if (screenWidth > 600) 28.sp else 24.sp),
                    color = Color(0xFF74558B),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.we_are_thrilled),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = if (screenWidth > 600) 18.sp else 16.sp),
                    color = Color(0xFF74558B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Input fields
                InputField(
                    label = stringResource(R.string.name),
                    placeholder = stringResource(R.string.name_input),
                    value = name,
                    onValueChange = { name = it }
                )
                nameError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                InputField(
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.email_input),
                    onValueChange = { email = it },
                    value = email
                )
                emailError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                dropdownfielduseful(
                    label = stringResource(R.string.pronouns),
                    currentValue = selectedPronoun,
                    options = options,
                    onOptionSelected = { selectedPronoun = it }
                )
                Spacer(modifier = Modifier.height(12.dp))

                InputFieldAuth(
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.password_input),
                    isPassword = true,
                    helpertext = stringResource(R.string.password_helper),
                    value = password,
                    onValueChange = { password = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Register button
                Button(
                    onClick = {
                        if (validateInputs()) {
                            authViewModel.registerUser(email, password, name, selectedPronoun)
                            navController.navigate("homepage")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth(if (screenWidth > 600) 0.6f else 0.8f) // Adjust width for larger screens
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.register),
                        color = Color.White,
                        fontSize = if (screenWidth > 600) 18.sp else 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    val navController = rememberNavController()
    val homePageViewModel = viewModel<HomePageViewModel>()
    // this is a dummy navcontroller
    RegistrationScreen(navController);
}

