package ipp.estg.mobile.ui.screens.main.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipp.estg.mobile.data.retrofit.models.ad.AdResponse
import ipp.estg.mobile.ui.components.CustomAdBanner
import ipp.estg.mobile.ui.components.cards.PlantCard
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.AdViewModel
import ipp.estg.mobile.viewModels.PlantViewModel

@Composable
fun MainTab(navController: NavController, plantViewModel: PlantViewModel, adViewModel: AdViewModel) {

    val plants = plantViewModel.plants.collectAsState().value
    val isLoadingPlants = plantViewModel.isLoading.collectAsState().value
    val error = plantViewModel.error.collectAsState().value
    val userRolePaid = plantViewModel.userRolePaid
    val ad = adViewModel.ad.collectAsState()

    LaunchedEffect(Unit) {
        plantViewModel.getPlants()
        adViewModel.getRandomAd()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Almanac",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

//        PlantCard(
//            name = "Dwarf Bonsai",
//            plantImageRes = R.drawable.dwarfbonsai,
//            onClick = {}
//        )

        if (isLoadingPlants) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            PlantsInormation(
                plants = plants,
                navController = navController,
                userRolePaid = userRolePaid,
                ad = ad.value
            )
        }

        if (error.isNotEmpty()) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun PlantsInormation(plants: List<ipp.estg.mobile.data.retrofit.models.plant.PlantResponse>, navController: NavController, userRolePaid: Boolean, ad:  AdResponse?) {
    plants.forEachIndexed { index, plant ->
        PlantCard(
            name = plant.name,
            plantImageUrl = plant.plantImage,
            onClick = {
                navController.navigate(
                    Screen.PlantInformation.route + "/${plant.id}"
                )
            }
        )

        // Mostrar anúncio personalizado a cada 2 plantas para utilizadores free
        if (!userRolePaid && (index + 1) % 2 == 0 && index < plants.size - 1) {
            if(ad != null) {
                CustomAdBanner(ad = ad)
            }

        }
    }
}

