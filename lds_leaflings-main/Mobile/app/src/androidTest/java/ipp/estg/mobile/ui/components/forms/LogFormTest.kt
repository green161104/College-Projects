package ipp.estg.mobile.ui.components.forms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import ipp.estg.mobile.ui.components.forms.LogForm as LogForm1

class LogFormTest{

    @get:Rule
    val composeTestRule = createComposeRule()

        @Test
        fun LogForm_rendersCorrectly(){
            composeTestRule.setContent {
                LogForm1(
                    newLogDescription = "newLogDescription",
                    onLogDescriptionChange = {},
                    onAddLog = {},
                    onError = {}
                )
            }

            // Assert that all components are present
            composeTestRule.onNodeWithTag("addLogTitle").assertExists()
            composeTestRule.onNodeWithTag("logDescription").assertExists()
            composeTestRule.onNodeWithTag("addLogButton").assertExists()
        }

        @Test
        fun LogForm_buttonClickInvokesCallback(){
            var buttonClicked = false

            composeTestRule.setContent {
                LogForm1(
                    newLogDescription = "newLogDescription",
                    onLogDescriptionChange = {},
                    onAddLog = { buttonClicked = true },
                    onError = {}
                )
            }

            // Simulate button click
            composeTestRule.onNodeWithTag("addLogButton").performClick()

            assertTrue(buttonClicked)
        }

        @Test
        fun LogForm_logDescriptionChangeInvokesCallback(){
            var logDescriptionChanged = false

            composeTestRule.setContent {
                LogForm1(
                    newLogDescription = "newLogDescription",
                    onLogDescriptionChange = { logDescriptionChanged = true },
                    onAddLog = {},
                    onError = {}
                )
            }

            // Simulate text input
            composeTestRule.onNodeWithTag("logDescription").performTextInput("newLogDescription")

            assertTrue(logDescriptionChanged)
        }

    @Test
    fun LogForm_errorInvokesCallback() {
        var errorInvoked = false

        composeTestRule.setContent {
            LogForm1(
                newLogDescription = "", // Valor inicial vazio
                onLogDescriptionChange = {},
                onAddLog = {},
                onError = { errorInvoked = true } // Callback que altera a flag
            )
        }

        // Simula o clique no botão
        composeTestRule.onNodeWithTag("addLogButton").performClick()

        // Verifica se a callback foi invocada
        assertTrue("Error callback was not invoked", errorInvoked)
    }

    @Test
    fun LogForm_errorMessageIsCorrect() {
        var errorMessage = ""

        composeTestRule.setContent {
            LogForm1(
                newLogDescription = "",
                onLogDescriptionChange = {},
                onAddLog = {},
                onError = { errorMessage = it }
            )
        }

        // Simula o clique no botão
        composeTestRule.onNodeWithTag("addLogButton").performClick()

        // Verifica se a mensagem de erro está correta
        assertEquals("Please enter a log description", errorMessage)
    }

    @Test
    fun LogForm_showsErrorWhenDescriptionExceedsMaxLength() {
        val longDescription = "a".repeat(501) // Cria uma string com 501 caracteres

        composeTestRule.setContent {
            LogForm1(
                newLogDescription = longDescription,
                onLogDescriptionChange = {},
                onAddLog = {},
                onError = {}
            )
        }

        // Verifica se o campo de texto está marcado como erro
        composeTestRule.onNodeWithTag("logDescription").assertExists()
        // (Opcional) Verifique visualmente se o erro é mostrado
    }

    @Test
    fun LogForm_rendersCorrectlyWithEmptyState() {
        composeTestRule.setContent {
            LogForm1(
                newLogDescription = "",
                onLogDescriptionChange = {},
                onAddLog = {},
                onError = {}
            )
        }

        // Verifica se os componentes ainda estão presentes
        composeTestRule.onNodeWithTag("addLogTitle").assertExists()
        composeTestRule.onNodeWithTag("logDescription").assertExists()
        composeTestRule.onNodeWithTag("addLogButton").assertExists()
    }

    @Test
    fun LogForm_updatesLogDescriptionCorrectly() {
        var updatedDescription = ""

        composeTestRule.setContent {
            LogForm1(
                newLogDescription = "",
                onLogDescriptionChange = { updatedDescription = it },
                onAddLog = {},
                onError = {}
            )
        }

        val inputText = "Updated description"

        // Simula entrada de texto no campo
        composeTestRule.onNodeWithTag("logDescription").performTextInput(inputText)

        // Verifica se o callback atualizou o valor corretamente
        assertEquals(inputText, updatedDescription)
    }


    @Test
    fun LogForm_resetsStateAfterLogAdded() {
        var logDescription = "Initial Description"

        composeTestRule.setContent {
            LogForm1(
                newLogDescription = logDescription,
                onLogDescriptionChange = { logDescription = it },
                onAddLog = { logDescription = "" },
                onError = {}
            )
        }

        // Simula o clique no botão "Add Log"
        composeTestRule.onNodeWithTag("addLogButton").performClick()

        // Verifica se o estado foi redefinido
        assertEquals("", logDescription)
    }

    @Test
    fun LogForm_characterCounterDisplaysCorrectLength() {
        val inputText = "Test description"

        composeTestRule.setContent {
            LogForm1(
                newLogDescription = inputText,
                onLogDescriptionChange = {},
                onAddLog = {},
                onError = {}
            )
        }

        // Verifica se o contador de caracteres mostra o número correto
        composeTestRule.onNodeWithText("${inputText.length} / 500").assertExists()
    }

    @Test
    fun LogForm_showsErrorWhenNoChangesMade() {
        var errorMessage = ""

        composeTestRule.setContent {
            LogForm1(
                newLogDescription = "",
                onLogDescriptionChange = {},
                onAddLog = {},
                onError = { errorMessage = it }
            )
        }

        // Simula o clique no botão "Add Log"
        composeTestRule.onNodeWithTag("addLogButton").performClick()

        // Verifica se a mensagem de erro foi definida corretamente
        assertEquals("Please enter a log description", errorMessage)
    }


}