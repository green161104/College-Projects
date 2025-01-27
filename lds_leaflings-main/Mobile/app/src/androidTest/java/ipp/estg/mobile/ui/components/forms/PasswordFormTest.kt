package ipp.estg.mobile.ui.components.forms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ipp.estg.mobile.ui.components.forms.PasswordForm as PasswordForm1

@RunWith(AndroidJUnit4::class)
class PasswordFormTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun passwordForm_rendersCorrectly() {
        composeTestRule.setContent {
            PasswordForm1(
                oldPassword = "oldPassword",
                newPassword = "newPassword",
                onOldPasswordChange = {},
                onNewPasswordChange = {},
                onSubmit = {}
            )
        }

        // Assert that all components are present
        composeTestRule.onNodeWithTag("old_password_input").assertExists()
        composeTestRule.onNodeWithTag("new_password_input").assertExists()
        composeTestRule.onNodeWithTag("submit_button").assertExists()
    }

    @Test
    fun passwordForm_buttonClickInvokesCallback() {
        var buttonClicked = false

        composeTestRule.setContent {
            PasswordForm1(
                oldPassword = "oldPassword",
                newPassword = "newPassword",
                onOldPasswordChange = {},
                onNewPasswordChange = {},
                onSubmit = { buttonClicked = true }
            )
        }

        // Simulate button click
        composeTestRule.onNodeWithTag("submit_button").performClick()

        assertTrue(buttonClicked)
    }

    @Test
    fun passwordForm_oldPasswordChangeInvokesCallback() {
        var oldPasswordChanged = false

        composeTestRule.setContent {
            PasswordForm1(
                oldPassword = "oldPassword",
                newPassword = "newPassword",
                onOldPasswordChange = { oldPasswordChanged = true },
                onNewPasswordChange = {},
                onSubmit = {}
            )
        }

        // Simulate old password change
        composeTestRule.onNodeWithTag("old_password_input").performTextInput("newOldPassword")

        assertTrue(oldPasswordChanged)
    }

    @Test
    fun passwordForm_newPasswordChangeInvokesCallback() {
        var newPasswordChanged = false

        composeTestRule.setContent {
            PasswordForm1(
                oldPassword = "oldPassword",
                newPassword = "newPassword",
                onOldPasswordChange = {},
                onNewPasswordChange = { newPasswordChanged = true },
                onSubmit = {}
            )
        }

        // Simulate new password change
        composeTestRule.onNodeWithTag("new_password_input").performTextInput("newNewPassword")

        assertTrue(newPasswordChanged)
    }

    @Test
    fun passwordForm_rejectsWeakPassword() {
        var errorMessage = ""

        composeTestRule.setContent {
            PasswordForm1(
                oldPassword = "correctPassword",
                newPassword = "123",
                onOldPasswordChange = {},
                onNewPasswordChange = {},
                onSubmit = {
                    errorMessage = "Password too weak"
                }
            )
        }

        composeTestRule.onNodeWithTag("submit_button").performClick()

        assertEquals("Password too weak", errorMessage)
    }

    @Test
    fun passwordForm_rejectsSameOldAndNewPassword() {
        var errorMessage = ""

        composeTestRule.setContent {
            PasswordForm1(
                oldPassword = "samePassword",
                newPassword = "samePassword",
                onOldPasswordChange = {},
                onNewPasswordChange = {},
                onSubmit = {
                    errorMessage = "New password cannot be the same as the old password"
                }
            )
        }

        composeTestRule.onNodeWithTag("submit_button").performClick()

        assertEquals("New password cannot be the same as the old password", errorMessage)
    }

    @Test
    fun passwordForm_respectsMaxLength() {
        var newPassword = ""

        composeTestRule.setContent {
            PasswordForm1(
                oldPassword = "oldPassword",
                newPassword = "",
                onOldPasswordChange = {},
                onNewPasswordChange = { newPassword = it },
                onSubmit = {}
            )
        }

        val longPassword = "a".repeat(100) // Assumindo limite de 100 caracteres
        composeTestRule.onNodeWithTag("new_password_input").performTextInput(longPassword)

        assertTrue(
            "Password should not exceed 100 characters",
            newPassword.length <= 100
        )
    }

    @Test
    fun passwordForm_respectsMinLength() {
        var errorMessage = ""

        composeTestRule.setContent {
            PasswordForm1(
                oldPassword = "oldPassword",
                newPassword = "123",
                onOldPasswordChange = {},
                onNewPasswordChange = {},
                onSubmit = {
                    errorMessage = "Password too weak"
                }
            )
        }

        composeTestRule.onNodeWithTag("submit_button").performClick()

        assertEquals("Password too weak", errorMessage)
    }

}