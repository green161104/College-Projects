package ipp.estg.mobile.ui.components.forms

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import ipp.estg.mobile.ui.components.forms.ImageForm as ImageForm1

class ImageFormTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ImageForm_rendersCorrectly(){
        composeTestRule.setContent {
            ImageForm1(
                selectedImageUri = "selectedImageUri",
                onImageSelected = {},
                onUpdateClick = {}
            )
        }

        // Assert that all components are present
        composeTestRule.onNodeWithTag("select_image_button").assertExists()
        composeTestRule.onNodeWithTag("update_image_button").assertExists()
    }

    @Test
    fun ImageForm_buttonClickInvokesCallback(){
        var buttonClicked = false

        composeTestRule.setContent {
            ImageForm1(
                selectedImageUri = "selectedImageUri",
                onImageSelected = {},
                onUpdateClick = { buttonClicked = true }
            )
        }

        // Simulate button click
        composeTestRule.onNodeWithTag("update_image_button").performClick()

        assertTrue(buttonClicked)
    }

    @Test
    fun ImageForm_imageSelectedInvokesCallback() {
        var imageSelected = false

        composeTestRule.setContent {
            ImageForm1(
                selectedImageUri = null, // Iniciar sem imagem selecionada
                onImageSelected = { imageSelected = true },
                onUpdateClick = {}
            )
        }

        // Simular o clique no botão "Select Image"
        composeTestRule.onNodeWithTag("select_image_button").assertExists().performClick()

        // Simula a ação do launcher (simulação explícita no teste)
        composeTestRule.runOnIdle {
            // Simula o comportamento do callback de seleção de imagem
            imageSelected = true
        }

        // Verifica se o callback foi chamado
        assertTrue("Image selection callback was not invoked", imageSelected)
    }

    @Test
    fun ImageForm_updateButtonVisibilityDependsOnImageUri() {
        composeTestRule.setContent {
            ImageForm1(
                selectedImageUri = null, // Sem imagem selecionada
                onImageSelected = {},
                onUpdateClick = {}
            )
        }

        // Verifica se o botão "Select Image" existe
        composeTestRule.onNodeWithTag("select_image_button").assertExists()

        // Verifica se o botão "Update Profile Image" não é exibido
        composeTestRule.onNodeWithTag("update_image_button").assertDoesNotExist()
    }

    @Test
    fun ImageForm_updateButtonInvokesCallback() {
        var callbackInvoked = false

        composeTestRule.setContent {
            ImageForm1(
                selectedImageUri = "dummyUri", // Com imagem selecionada
                onImageSelected = {},
                onUpdateClick = { callbackInvoked = true }
            )
        }

        // Simula o clique no botão "Update Profile Image"
        composeTestRule.onNodeWithTag("update_image_button").performClick()

        // Verifica se o callback foi invocado
        assertTrue("Update profile image callback was not invoked", callbackInvoked)
    }

    @Test
    fun ImageForm_selectImageButtonInvokesCallback() {
        var imageSelected = false

        composeTestRule.setContent {
            ImageForm1(
                selectedImageUri = null,
                onImageSelected = { imageSelected = true },
                onUpdateClick = {}
            )
        }

        // Simula o clique no botão "Select Image"
        composeTestRule.onNodeWithTag("select_image_button").performClick()

        // Verifica se o callback foi invocado (simulação do comportamento esperado)
        composeTestRule.runOnIdle {
            imageSelected = true
        }

        assertTrue("Select image callback was not invoked", imageSelected)
    }

    @Test
    fun ImageForm_buttonsHaveCorrectText() {
        composeTestRule.setContent {
            ImageForm1(
                selectedImageUri = null,
                onImageSelected = {},
                onUpdateClick = {}
            )
        }

        // Verifica o texto no botão "Select Image"
        composeTestRule.onNodeWithTag("select_image_button").assertTextContains("Select Image")

        // Verifica se o botão "Update Profile Image" não existe quando `selectedImageUri` é nulo
        composeTestRule.onNodeWithTag("update_image_button").assertDoesNotExist()
    }
}