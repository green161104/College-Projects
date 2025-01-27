package ipp.estg.mobile.ui.screens

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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ipp.estg.mobile.data.retrofit.models.diary.DiaryRequest
import ipp.estg.mobile.ui.components.images.PlantImage
import ipp.estg.mobile.ui.components.PlantTasks
import ipp.estg.mobile.ui.components.navigation.CustomTopAppBar
import ipp.estg.mobile.ui.components.utils.DeleteButton
import ipp.estg.mobile.ui.components.utils.LightSquaredButton
import ipp.estg.mobile.viewModels.DiaryViewModel
import ipp.estg.mobile.viewModels.PlantViewModel
import ipp.estg.mobile.viewModels.UserPlantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPlantScreen(
    plantId: Int,
    userPlantId: Int,
    plantViewModel: PlantViewModel = viewModel(),
    userPlantViewModel: UserPlantViewModel = viewModel(),
    diaryViewModel: DiaryViewModel = viewModel(),
    goBack: () -> Unit,
    goToLogs: (userPlantId: Int) -> Unit
) {

    val plant = plantViewModel.plant.collectAsState().value
    val tasks = plantViewModel.tasks.collectAsState().value
    val diary = diaryViewModel.diary.collectAsState().value

    LaunchedEffect(Unit) {
        plantViewModel.getPlantById(plantId)
        diaryViewModel.getDiary(userPlantId)
        plantViewModel.getPlantTasks(plantId)
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = plant?.name,
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

            PlantTasks(
                tasks = tasks,
            )

            Spacer(modifier = Modifier.height(40.dp))

            DiarySection(
                diary = diary,
                userPlantId = userPlantId,
                insertDiary = { diary ->
                    diaryViewModel.addDiary(
                        diary = diary,
                        onSuccess = { addedDiary ->
                            goToLogs(addedDiary.id)
                        }
                    )
                },
                goToLogs = {
                    diary?.id?.let { goToLogs(it) }
                }
            )


            DeleteButton(
                onClick = {
                    userPlantViewModel.deleteUserPlant(plantId)
                    goBack()
                }
            )

        }
    }
}


@Composable
private fun Header(plantName: String, plantImageUrl: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 50.dp)
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
fun DiarySection(diary: ipp.estg.mobile.data.retrofit.models.diary.DiaryResponse?, userPlantId: Int, insertDiary: (diary: ipp.estg.mobile.data.retrofit.models.diary.DiaryRequest) -> Unit, goToLogs: () -> Unit) {
    val showTextField = remember { mutableStateOf(false) }
    val diaryName = remember { mutableStateOf("") }

    if (diary == null) {
        if (showTextField.value) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = diaryName.value,
                    onValueChange = { diaryName.value = it },
                    label = { Text("Diary Name") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                LightSquaredButton(
                    text = "Create",
                    onClick = {
                        val newDiary = DiaryRequest(
                            userPlantId,
                            diaryName.value
                        )
                        insertDiary(newDiary)
                    }
                )
            }
        } else {
            LightSquaredButton(
                text = "Create Diary",
                onClick = {
                    showTextField.value = true
                }
            )
        }
    } else {
        LightSquaredButton(
            text = "See Diary",
            onClick = {
                goToLogs()
            }
        )
    }

}