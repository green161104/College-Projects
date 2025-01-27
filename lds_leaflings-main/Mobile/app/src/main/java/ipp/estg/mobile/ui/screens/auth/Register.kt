package ipp.estg.mobile.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.mobile.R
import ipp.estg.mobile.data.retrofit.models.register.RegisterRequest
import ipp.estg.mobile.ui.components.forms.PreferenccesForm
import ipp.estg.mobile.ui.components.utils.LightSquaredButton
import ipp.estg.mobile.ui.components.utils.Loading
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.AuthViewModel


@Composable
fun RegisterPage(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {

    val context = LocalContext.current
    val error = authViewModel.error.collectAsState().value
    val isLoading = authViewModel.isLoading.collectAsState().value

    var formData: RegisterRequest by remember { mutableStateOf(RegisterRequest()) }

    // Controla o passo atual
    var currentStep by remember { mutableIntStateOf(1) }

    // Observa o estado de erro e atualiae o `currentStep` quando houver erro
    LaunchedEffect(authViewModel.error) {
        if (error.isNotEmpty()) {
            currentStep = 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()

        when (currentStep) {
            1 -> RegisterStepOne(formData) { updatedFormData ->
                formData = updatedFormData
                currentStep++
            }

            2 -> RegisterStepTwo(formData) { updatedFormData ->
                formData = updatedFormData
                authViewModel.register(
                    registerRequest = formData,
                    onSuccess = {
                        Toast.makeText(context, "Register successful", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Login.route)
                    }
                )
            }

        }


        // Mostra um Loading
        if (isLoading) {
            Loading()
        }

        // Mostra uma mensagem de erro
        Text(
            text = error, color = MaterialTheme.colorScheme.error,
            modifier = Modifier.testTag("error_message")
        )

        TermsOfService()

        Spacer(modifier = Modifier.height(50.dp))

    }
}

@Composable
private fun Header() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 55.dp, horizontal = 40.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.leaflings_logo),
            contentDescription = "Leaflings Logo",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "Register", fontSize = 55.sp, textAlign = TextAlign.Center
        )

    }

}

@Composable
fun RegisterStepOne(formData: RegisterRequest, onNext: (RegisterRequest) -> Unit) {
    var username by remember { mutableStateOf(formData.username) }
    var email by remember { mutableStateOf(formData.email) }
    var password by remember { mutableStateOf(formData.password) }
    var location by remember { mutableStateOf(formData.location) }
    var contact by remember { mutableStateOf(formData.contact) }

    TextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") },
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .testTag("username_input")
    )
    Spacer(modifier = Modifier.height(30.dp))

    TextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .testTag("email_input")
    )
    Spacer(modifier = Modifier.height(30.dp))

    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .testTag("password_input")
    )

    Spacer(modifier = Modifier.height(30.dp))

    TextField(
        value = location,
        onValueChange = { location = it },
        label = { Text("Location") },
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .testTag("location_input")
    )

    Spacer(modifier = Modifier.height(30.dp))

    TextField(
        value = contact,
        onValueChange = { contact = it },
        label = { Text("Contact") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .testTag("contact_input")
    )

    Spacer(modifier = Modifier.height(45.dp))

    // Register Button
    LightSquaredButton(
        text = "Register",
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(80.dp)
            .padding(vertical = 10.dp)
            .testTag("register_button"),
        fontWeight = FontWeight.Normal,
        textSize = 30,
        onClick = {
            onNext(
                formData.copy(
                    username = username,
                    email = email,
                    password = password,
                    location = location,
                    contact = contact
                )
            )
        },
    )
}

@Composable
fun RegisterStepTwo(formData: RegisterRequest, onNext: (RegisterRequest) -> Unit) {
    var waterAvailability by remember { mutableStateOf(formData.waterAvailability) }
    var careExperience by remember { mutableStateOf(formData.careExperience) }
    var luminosityAvailability by remember { mutableStateOf(formData.luminosityAvailability) }


    PreferenccesForm(
        buttonText = "Register",
        waterAvailability = waterAvailability,
        careExperience = careExperience,
        luminosityAvailability = luminosityAvailability,
        onWaterAvailabilitySelected = { waterAvailability = it },
        onCareExperienceSelected = { careExperience = it },
        onLuminosityAvailabilitySelected = { luminosityAvailability = it },
        onButtonClicked = {
            onNext(
                formData.copy(
                    waterAvailability = waterAvailability,
                    careExperience = careExperience,
                    luminosityAvailability = luminosityAvailability
                )
            )
        }
    )
}


@Composable
private fun TermsOfService() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 40.dp),
    ) {
        Text(
            text = buildAnnotatedString {
                append("By clicking Register, you agree to Leadflings's ")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Terms of Service")
                }
                append(" and ")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Privacy Policy")
                }
            }, fontSize = 15.sp, textAlign = TextAlign.Center
        )
    }
}