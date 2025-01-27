package ipp.estg.mobile.ui.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LogForm(
    newLogDescription: String,
    onLogDescriptionChange: (String) -> Unit,
    onAddLog: () -> Unit,
    onError: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Add New Log",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
                    .testTag("addLogTitle")
            )

            TextField(
                value = newLogDescription,
                onValueChange = { onLogDescriptionChange(it) },
                label = { Text("Log Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .testTag("logDescription"),
                supportingText = {
                    Text(
                        text = "${newLogDescription.length} / 500",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                isError = newLogDescription.length > 500
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (newLogDescription.isNotBlank()) {
                        onAddLog()
                    } else {
                        onError("Please enter a log description")
                    }
                },
                modifier = Modifier.align(Alignment.End)
                    .testTag("addLogButton")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Log")

                Spacer(Modifier.width(8.dp))

                Text("Add Log")
            }
        }
    }
}