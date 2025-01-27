package ipp.estg.mobile.ui.screens.main.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ipp.estg.mobile.ui.components.images.ProfileImage
import ipp.estg.mobile.ui.components.UserPlants
import ipp.estg.mobile.ui.components.cards.AddPlantCard
import ipp.estg.mobile.ui.components.utils.Loading
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.UserPlantViewModel
import ipp.estg.mobile.viewModels.UserViewModel

@Composable
fun ProfileTab(
    userPlantsViewModel: UserPlantViewModel, userViewModel: UserViewModel,
    navController: NavController
) {

    val userPlants = userPlantsViewModel.userPlants.collectAsState().value
    val isLoadingUserPlants = userPlantsViewModel.isLoading.collectAsState().value
    val user = userViewModel.user.collectAsState().value
    val error = userPlantsViewModel.error.collectAsState().value

    LaunchedEffect(Unit) {
        userViewModel.getUserInfo()
        userPlantsViewModel.getUserPlants()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 25.dp, horizontal = 10.dp),
    ) {

        if (user != null) {
            Header(
                user = user,
                goToProfile = {
                    navController.navigate(Screen.ProfileScreen.route)
                }
            )
        }
        else {
            Loading()
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "My Plants:",
            fontSize = 28.sp,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(40.dp))

        if (isLoadingUserPlants) {
            Loading()
        } else {
            // Display user plants
            UserPlants(
                userPlants = userPlants,
                goToUserPlantScreen = { plantId, userPlantId ->
                    navController.navigate(Screen.UserPlant.route + "/${plantId}/${userPlantId}")
                }
            )

            // Botão de adicionar planta, não vai ser mostrado se um utilizador não pago tiver 3 userPlants
            if (user?.rolePaid == true || userPlants.size < 3) {
                AddPlantCard(
                    onClick = {
                        // Navigate to plant match tab in the mainScreen
                        navController.navigate(Screen.Main.route + "/2")
                    }
                )
            }
        }


        if (error.isNotEmpty()) {
            Text(
                text = error,
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }


    }
}


@Composable
private fun Header(user: ipp.estg.mobile.data.retrofit.models.user.UserResponse, goToProfile: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { goToProfile() }
            .padding(horizontal = 10.dp)
    ) {
        ProfileImage(
            path = user.userAvatar,
            contentDescription = user.username,
            modifier = Modifier
                .size(125.dp)
                .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(10.dp))


        Text(
            text = user.username,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}