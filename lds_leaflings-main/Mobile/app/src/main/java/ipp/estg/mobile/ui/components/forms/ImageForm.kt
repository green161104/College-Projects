package ipp.estg.mobile.ui.components.forms

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipp.estg.mobile.ui.components.utils.LightSquaredButton

@Composable
fun ImageForm(
    selectedImageUri: String?,
    onImageSelected: (String) -> Unit,
    onUpdateClick: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.toString()?.let { onImageSelected(it) }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 65.dp)
            .fillMaxWidth()
    ) {
        LightSquaredButton(
            text = "Select Image",
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(80.dp)
                .padding(vertical = 10.dp)
                .testTag("select_image_button"),
            textModifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Normal,
            textSize = 30,
            onClick = {
                launcher.launch("image/*")
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (selectedImageUri != null) {
            LightSquaredButton(
                text = "Update Profile Image",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(80.dp)
                    .padding(vertical = 10.dp)
                    .testTag("update_image_button"),
                textModifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Normal,
                textSize = 30,
                onClick = onUpdateClick
            )
        }
    }


}