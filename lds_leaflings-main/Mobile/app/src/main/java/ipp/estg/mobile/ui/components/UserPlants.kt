package ipp.estg.mobile.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ipp.estg.mobile.ui.components.cards.PlantCard

@Composable
fun UserPlants(userPlants: List<ipp.estg.mobile.data.retrofit.models.userPlant.UserPlantResponse>, goToUserPlantScreen: (plantId: Int, userPlantId: Int) -> Unit) {
    userPlants.forEach { userPlant ->
        PlantCard(
            name = userPlant.plant.name,
            plantImageUrl = userPlant.plant.plantImage,  // You might want to use actual plant image here
            onClick = {
                goToUserPlantScreen(userPlant.plant.id, userPlant.id)
            }
        )

        Spacer(modifier = Modifier.width(10.dp))
    }
}