package ipp.estg.mobile.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ipp.estg.mobile.ui.components.utils.LightSquaredButton

@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 65.dp)
            .fillMaxWidth()
    ) {
        // Email TextField
        TextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(0.85f)// Adjust the width (85% of screen width)
                .testTag("email_input")
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Hide password text
            modifier = Modifier.fillMaxWidth(0.85f) // Same width as email field
                .testTag("password_input")

        )

        Spacer(modifier = Modifier.height(30.dp))


        LightSquaredButton(
            text = "Login",
            modifier = Modifier
                .fillMaxWidth(0.85f) // Make the button the same width as the TextFields
                .height(80.dp) // Adjust height if needed
                .padding(vertical = 10.dp)
                .testTag("login_button"),
            textModifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Normal,
            textSize = 30,
            onClick = {
                onLoginClick()
            }
        )
    }
}