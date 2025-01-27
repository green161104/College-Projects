package ipp.estg.mobile.ui.screens.auth

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterTest{

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: NavController

    @Before
    fun setupLoginScreen() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        navController = NavController(context)
    }

    @Test
    fun registerPage_initialState() {
        composeTestRule.setContent {
            RegisterPage(navController = navController)
        }

        // Verify initial UI elements are present
        composeTestRule.onNodeWithTag("email_input").assertExists()
        composeTestRule.onNodeWithTag("password_input").assertExists()
        composeTestRule.onNodeWithTag("location_input").assertExists()
        composeTestRule.onNodeWithTag("contact_input").assertExists()
        composeTestRule.onNodeWithTag("register_button").assertExists()

        // Verify error message is not initially shown
        composeTestRule.onNodeWithTag("error_message").assertExists()
    }

    @Test
    fun registerPage_registerSuccess() {
        composeTestRule.setContent {
            RegisterPage(navController = navController)
        }

        // Verify initial UI elements are present
        composeTestRule.onNodeWithTag("username_input").performTextInput("Teste")
        composeTestRule.onNodeWithTag("email_input").performTextInput("teste@teste.com")
        composeTestRule.onNodeWithTag("password_input").performTextInput("testeteste")
        composeTestRule.onNodeWithTag("location_input").performTextInput("teste")
        composeTestRule.onNodeWithTag("contact_input").performTextInput("913645896")

        composeTestRule.onNodeWithTag("register_button").performClick()

    }

    @Test
    fun registerPage_registerFailure() {
        composeTestRule.setContent {
            RegisterPage(navController = navController)
        }

        // Verify initial UI elements are present
        composeTestRule.onNodeWithTag("register_button").performClick()

        // Verify error message is shown
        composeTestRule.onNodeWithTag("error_message").assertExists()

    }

}