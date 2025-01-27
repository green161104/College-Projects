package com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables

import android.service.autofill.OnClickAction
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun AppButton(
    onClickAction: () -> Unit, // Define the onClick parameter as a lambda
    buttonText: String,
    icon: (@Composable () -> Unit)? = null
) {
    ElevatedButton(
        onClick = {
            onClickAction()
        },
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF5C8E61)),
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .padding(8.dp)
            .shadow(10.dp, RoundedCornerShape(50.dp))

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Include the icon if it's provided
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(buttonText, color = Color.White)
        }
    }
}
