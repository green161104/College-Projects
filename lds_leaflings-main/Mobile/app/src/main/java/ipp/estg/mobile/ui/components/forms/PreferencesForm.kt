package ipp.estg.mobile.ui.components.forms

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipp.estg.mobile.data.enums.CareExperience
import ipp.estg.mobile.data.enums.LuminosityAvailability
import ipp.estg.mobile.data.enums.WaterAvailability
import ipp.estg.mobile.ui.components.utils.LightSquaredButton
import ipp.estg.mobile.ui.components.utils.SuperUsefulDropDownMenuBox

@Composable
fun PreferenccesForm(
    buttonText: String,
    waterAvailability: String,
    careExperience: String,
    luminosityAvailability: String,
    onWaterAvailabilitySelected: (String) -> Unit,
    onCareExperienceSelected: (String) -> Unit,
    onLuminosityAvailabilitySelected: (String) -> Unit,
    onButtonClicked: () -> Unit
) {
    SuperUsefulDropDownMenuBox(label = "Experience with Plants",
        currentValue = careExperience,
        options = CareExperience.entries,
        modifier = Modifier.fillMaxWidth(0.85f)
            .testTag("care_experience_input"),
        onOptionSelected = { onCareExperienceSelected(it.toString()) })

    Spacer(modifier = Modifier.height(30.dp))

    SuperUsefulDropDownMenuBox(label = "Water Availability",
        currentValue = waterAvailability,
        options = WaterAvailability.entries,
        modifier = Modifier.fillMaxWidth(0.85f)
            .testTag("water_availability_input"),
        onOptionSelected = { onWaterAvailabilitySelected(it.toString()) })

    Spacer(modifier = Modifier.height(30.dp))

    SuperUsefulDropDownMenuBox(label = "Luminosity Availability",
        currentValue = luminosityAvailability,
        options = LuminosityAvailability.entries,
        modifier = Modifier.fillMaxWidth(0.85f)
            .testTag("luminosity_availability_input"),
        onOptionSelected = { onLuminosityAvailabilitySelected(it.toString()) })

    Spacer(modifier = Modifier.height(45.dp))

    // Register Button
    LightSquaredButton(
        text = buttonText,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(80.dp)
            .padding(vertical = 10.dp)
            .testTag("register_button"),
        fontWeight = FontWeight.Normal,
        textSize = 30,
        onClick = {
            onButtonClicked()
        }
    )
}