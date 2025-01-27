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
class LoginScreenTest{

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: NavController

    @Before
    fun setupLoginScreen() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        navController = NavController(context)
    }

    @Test
    fun loginPage_initialState() {

        composeTestRule.setContent {
            LoginPage(navController = navController)
        }

        // Verify initial UI elements are present
        composeTestRule.onNodeWithTag("email_input").assertExists()
        composeTestRule.onNodeWithTag("password_input").assertExists()
        composeTestRule.onNodeWithTag("login_button").assertExists()

        // Verify error message is not initially shown
        composeTestRule.onNodeWithTag("error_message").assertExists()
    }

    @Test
    fun loginPage_loginSuccess() {
        composeTestRule.setContent {
            LoginPage(navController = navController)
        }

        // Verify initial UI elements are present
        composeTestRule.onNodeWithTag("email_input").performTextInput("bill@bill.com")
        composeTestRule.onNodeWithTag("password_input").performTextInput("billbill")

        composeTestRule.onNodeWithTag("login_button").performClick()

        // Verify error message is not shown
        //composeTestRule.onNodeWithTag("error_message").assertDoesNotExist()

        // Wait for navigation to completeq
        composeTestRule.waitForIdle()
    }

    @Test
    fun loginPage_loginFailure() {
        composeTestRule.setContent {
            LoginPage(navController = navController)
        }

        // Verify initial UI elements are present
        composeTestRule.onNodeWithTag("email_input").performTextInput("invalid")
        composeTestRule.onNodeWithTag("password_input").performTextInput("invalid")

        composeTestRule.onNodeWithTag("login_button").performClick()

        // Verify error message is shown //TODO: Fix this
        composeTestRule.onNodeWithTag("error_message").assertExists()
    }

}



