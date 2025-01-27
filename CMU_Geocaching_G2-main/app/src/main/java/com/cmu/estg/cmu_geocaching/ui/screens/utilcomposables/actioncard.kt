package com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionCard(
    title: String,
    points: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    // Get screen width for responsive adjustments
    val screenWidth = LocalConfiguration.current.screenWidthDp

    // Dynamic padding and font size for small devices
    val horizontalPadding = if (screenWidth < 360) 12.dp else 16.dp
    val verticalPadding = if (screenWidth < 360) 12.dp else 16.dp
    val textSize = if (screenWidth < 360) 12.sp else 16.sp
    val buttonPadding = if (screenWidth < 360) 8.dp else 16.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        shape = RoundedCornerShape(8.dp),

    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Title and points section
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, fontSize = textSize)
                Text(text = points, color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Button with icon
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(backgroundColor),
                modifier = Modifier.padding(start = buttonPadding)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Go",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 4.dp) // Spacing between text and icon
                )
            }
        }
    }
}

@Preview
@Composable
fun ActionCardPreview() {
    ActionCard(
        title = "Sample Title",
        points = "234 Points",
        backgroundColor = Color.Gray,
        onClick = { /* Do something */ }
    )
}
