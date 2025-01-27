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
fun PasswordForm(
    oldPassword: String,
    newPassword: String,
    onOldPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 65.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = oldPassword,
            onValueChange = { onOldPasswordChange(it) },
            label = { Text("Current Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.85f)
                .testTag("old_password_input")
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = newPassword,
            onValueChange = { onNewPasswordChange(it) },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.85f)
                .testTag("new_password_input")
        )

        Spacer(modifier = Modifier.height(30.dp))

        LightSquaredButton(
            text = "Change Password",
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(80.dp)
                .padding(vertical = 10.dp)
                .testTag("submit_button"),
            textModifier = Modifier,
            fontWeight = FontWeight.Normal,
            textSize = 30,
            onClick = { onSubmit() }
        )
    }
}