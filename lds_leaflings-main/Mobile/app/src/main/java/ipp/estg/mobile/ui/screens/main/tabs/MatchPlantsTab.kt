package ipp.estg.mobile.ui.screens.main.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipp.estg.mobile.data.retrofit.models.plant.PlantResponse
import ipp.estg.mobile.ui.components.images.PlantImage
import ipp.estg.mobile.viewModels.UserViewModel

@Composable
fun MatchingPlantsTab(
    userViewModel: UserViewModel,
    goToPlantInfo: (plantId: Int) -> Unit
) {
    var perfectMatches: List<PlantResponse>? by remember { mutableStateOf(emptyList()) }
    var averageMatches: List<PlantResponse>? by remember { mutableStateOf(emptyList()) }
    var weakMatches: List<PlantResponse>? by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        userViewModel.getMatchingPlants(
            onSuccess = { matches ->
                perfectMatches = matches.perfectMatches
                averageMatches = matches.averageMatches
                weakMatches = matches.weakMatches
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        MatchSectionHeader("Perfect Matches (3/3)")
        perfectMatches?.forEach { match ->
            PlantMatchCard(
                match = match,
                goToPlantInfo = { goToPlantInfo(match.id) }
            )
        }

        MatchSectionHeader("Average Matches (2/3)")
        averageMatches?.forEach { match ->
            PlantMatchCard(
                match = match,
                goToPlantInfo = { goToPlantInfo(match.id) }
            )
        }

        MatchSectionHeader("Weak Matches (1/3)")
        weakMatches?.forEach { match ->
            PlantMatchCard(
                match = match,
                goToPlantInfo = { goToPlantInfo(match.id) }
            )
        }
    }
}

@Composable
fun MatchSectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun PlantMatchCard(match: PlantResponse, goToPlantInfo: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable { goToPlantInfo() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlantImage(
                path = match.plantImage,
                contentDescription = match.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = match.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = match.type,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Water Needs",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = match.waterNeeds,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Luminosity Needs",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = match.luminosityNeeded,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}