package ipp.estg.mobile.ui.components.forms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import ipp.estg.mobile.data.enums.CareExperience
import ipp.estg.mobile.data.enums.LuminosityAvailability
import ipp.estg.mobile.data.enums.WaterAvailability
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ipp.estg.mobile.ui.components.forms.PreferenccesForm as PreferenccesForm1

@RunWith(AndroidJUnit4::class)
class PreferencesFormTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun preferencesForm_rendersCorrectly() {
        composeTestRule.setContent {
            PreferenccesForm1(
                buttonText = "Submit",
                waterAvailability = WaterAvailability.LOW.name,
                careExperience = CareExperience.Beginner.name,
                luminosityAvailability = LuminosityAvailability.LOW.name,
                onWaterAvailabilitySelected = {},
                onCareExperienceSelected = {},
                onLuminosityAvailabilitySelected = {},
                onButtonClicked = {}
            )
        }

        // Assert that all components are present
        composeTestRule.onNodeWithTag("care_experience_input").assertExists()
        composeTestRule.onNodeWithTag("water_availability_input").assertExists()
        composeTestRule.onNodeWithTag("luminosity_availability_input").assertExists()
        composeTestRule.onNodeWithTag("register_button").assertExists()
    }

    @Test
    fun preferencesForm_buttonClickInvokesCallback() {
        var buttonClicked = false

        composeTestRule.setContent {
            PreferenccesForm1(
                buttonText = "Submit",
                waterAvailability = WaterAvailability.LOW.name,
                careExperience = CareExperience.Beginner.name,
                luminosityAvailability = LuminosityAvailability.LOW.name,
                onWaterAvailabilitySelected = {},
                onCareExperienceSelected = {},
                onLuminosityAvailabilitySelected = {},
                onButtonClicked = { buttonClicked = true }
            )
        }

        // Simulate button click
        composeTestRule.onNodeWithTag("register_button").performClick()
        assert(buttonClicked)
    }
}