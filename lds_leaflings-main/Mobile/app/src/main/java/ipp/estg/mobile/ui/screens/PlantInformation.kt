package ipp.estg.mobile.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ipp.estg.mobile.data.enums.CareExperience
import ipp.estg.mobile.data.enums.LuminosityAvailability
import ipp.estg.mobile.data.enums.WaterAvailability
import ipp.estg.mobile.ui.components.images.PlantImage
import ipp.estg.mobile.ui.components.StatItem
import ipp.estg.mobile.ui.components.navigation.CustomTopAppBar
import ipp.estg.mobile.ui.components.utils.LightSquaredButton
import ipp.estg.mobile.ui.components.utils.Loading
import ipp.estg.mobile.viewModels.PlantViewModel
import ipp.estg.mobile.viewModels.UserPlantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantInformation(
    plantViewModel: PlantViewModel = viewModel(),
    userPlantViewModel: UserPlantViewModel = viewModel(),
    plantId: Int,
    goBack: () -> Unit,
) {

    val currentContext = LocalContext.current
    val plant = plantViewModel.plant.collectAsState().value
    val isLoading = userPlantViewModel.isLoading.collectAsState().value
    val plantError = plantViewModel.error.collectAsState().value
    val userPlantError = userPlantViewModel.error.collectAsState().value

    LaunchedEffect(Unit) {
        plantViewModel.getPlantById(plantId)
    }


    LaunchedEffect(userPlantError) {
        if (userPlantError.isNotBlank()) {
            Toast.makeText(
                currentContext,
                userPlantError,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = plant?.name ?: "",
                onBackClick = {
                    goBack()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Header(
                plantName = plant?.name ?: "",
                plantImageUrl = plant?.plantImage ?: ""
            )

            Spacer(modifier = Modifier.height(30.dp))

            Stats(
                careExperience = plant?.expSuggested ?: CareExperience.Beginner.value,
                waterAvailability = plant?.waterNeeds ?: WaterAvailability.LOW.value,
                luminosityAvailability = plant?.luminosityNeeded
                    ?: LuminosityAvailability.LOW.value
            )

            Spacer(modifier = Modifier.height(20.dp))

            Description(plant?.description ?: "")

            Spacer(modifier = Modifier.height(40.dp))

            LightSquaredButton(
                text = "Add to my plants",
                onClick = {
                    userPlantViewModel.createUserPlant(
                        plantId = plantId,
                        onSuccess = {
                            goBack()
                        }
                    )
                }
            )

            if(isLoading) {
                Loading()
            }

            if (plantError.isNotBlank()) {
                Text(
                    text = plantError,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    color = Color.Red
                )
            }

        }
    }
}

@Composable
private fun Stats(
    careExperience: String,
    waterAvailability: String,
    luminosityAvailability: String
) {
    val animDelayPerItem = 100

    StatItem(
        statName = "Water",
        statValue = WaterAvailability.from(waterAvailability).ordinal + 1,
        statMaxValue = WaterAvailability.entries.size,
        statColor = Color.Blue,
        animDelay = 1 * animDelayPerItem,
    )

    Spacer(modifier = Modifier.height(10.dp))

    StatItem(
        statName = "Experience",
        statValue = CareExperience.from(careExperience).ordinal + 1,
        statMaxValue = CareExperience.entries.size,
        statColor = Color.Green,
        animDelay = 2 * animDelayPerItem
    )

    Spacer(modifier = Modifier.height(10.dp))

    StatItem(
        statName = "Sun Exposure",
        statValue = LuminosityAvailability.from(luminosityAvailability).ordinal + 1,
        statMaxValue = LuminosityAvailability.entries.size,
        statColor = Color.Yellow,
        animDelay = 3 * animDelayPerItem
    )
}

@Composable
private fun Header(plantName: String, plantImageUrl: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 60.dp)
    ) {

        PlantImage(
            path = plantImageUrl,
            contentDescription = plantName,
            modifier = Modifier
                .size(150.dp) // Set the size to 100.dp to make it smaller and square
                .clip(RoundedCornerShape(percent = 35)) // Add rounded borders with a radius of 8.dp
        )

        Text(
            text = plantName,
            fontSize = 35.sp,
            modifier = Modifier.padding(top = 15.dp)
        )
    }
}


@Composable
private fun Description(description: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
    ) {

        Text(
            text = description,
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Left,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 15.dp)
        )
    }
}