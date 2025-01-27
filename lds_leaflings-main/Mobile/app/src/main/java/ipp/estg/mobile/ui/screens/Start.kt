package ipp.estg.mobile.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.mobile.BuildConfig
import ipp.estg.mobile.R
import ipp.estg.mobile.ui.components.utils.LightSquaredButton
import ipp.estg.mobile.ui.theme.LightGreen
import ipp.estg.mobile.ui.theme.Orange
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.AuthViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StartPage(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        authViewModel.isLoggedIn(
            onSuccess = {
                navController.navigate(Screen.Main.route)
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        PageTop()

        PageBottom(navController)
    }
}

@Composable
private fun PageTop() {
    // Use Box to overlay components
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(655.dp) // Height of the background image
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.initial_bd_image),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize() // Fill the box with the image
        )

        // Logo and text on the middle-right
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.CenterEnd) // Aligns the column to the center-right of the Box
                .padding(end = 16.dp) // Optional: Add some padding from the right edge
        ) {
            Image(
                painter = painterResource(id = R.drawable.leaflings_logo),
                contentDescription = "Leaflings Logo",
                modifier = Modifier
                    .size(80.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Leaflings",
                fontSize = 45.sp,
                color = Orange,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PageBottom(navController: NavController) {
    val buttonModifiel = Modifier
        .padding(vertical = 11.dp, horizontal = 20.dp)
        .shadow(5.dp)
    val textModifier = Modifier.padding(horizontal = 19.dp, vertical = 5.dp)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {


        LightSquaredButton(
            "Login",
            buttonColors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
            textColor = Color.Black,
            buttonModifiel,
            textModifier,
            onClick = { navController.navigate(Screen.Login.route) }
        )
        LightSquaredButton(
            "Register",
            buttonColors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Black),
            textColor = Color.White,
            buttonModifiel, textModifier,
            onClick = { navController.navigate(Screen.Register.route) }
        )

    }
}