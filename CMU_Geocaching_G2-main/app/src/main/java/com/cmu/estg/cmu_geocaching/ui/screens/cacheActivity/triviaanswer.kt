package com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.viewModel.SharedCacheViewModel
import com.cmu.estg.cmu_geocaching.viewModel.TriviaQuestionViewModel
import com.cmu.estg.cmu_geocaching.viewModel.HomePageViewModel


@Preview(showBackground = true)
@Composable
fun triviaquestionPreview() {
    val navController = rememberNavController()
    val cache = Cache(
        cacheId = "1",
        createdBy = "1",
        difficulty = "Easy",
        latitude = "76.3827",
        longitude = "77.03921",
        rating = "1",
        image = "",
        description = "cache"
    )
    val sharedCacheViewModel: SharedCacheViewModel = viewModel()
    val sharedTriviaQuestionViewModel: TriviaQuestionViewModel = viewModel()
    sharedCacheViewModel.setSelectedCache(cache)
    val homePageViewModel = viewModel<HomePageViewModel>()

    QuestionAnswerScreen(
        sharedTriviaQuestionViewModel,
        navController,
        sharedCacheViewModel
    )
}

@Composable
fun QuestionAnswerScreen(
    sharedTriviaQuestionViewModel: TriviaQuestionViewModel,
    navController: NavController,
    sharedCacheViewModel: SharedCacheViewModel
) {
    val isCorrect = sharedTriviaQuestionViewModel.isAnswerCorrect.collectAsState().value
    val backgroundColor = if (isCorrect == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    val message = if (isCorrect == true) stringResource(id = com.cmu.estg.cmu_geocaching.R.string.awesome_job)
    else stringResource(id = com.cmu.estg.cmu_geocaching.R.string.missed_that_one)
    val addMessage =
        if (isCorrect == true) stringResource(id = com.cmu.estg.cmu_geocaching.R.string.well_done)
        else stringResource(id = com.cmu.estg.cmu_geocaching.R.string.better_luck)
    val points = if(isCorrect == true) 150 else 50
    val currentCache = sharedCacheViewModel.selectedCache.collectAsState()

    LaunchedEffect(key1 = isCorrect) {
        if (isCorrect != null) { // Optional: add checks to avoid null state
            sharedTriviaQuestionViewModel.addUserPointsAndCacheHistory(currentCache.value!!)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(Brush.verticalGradient(listOf(backgroundColor, MaterialTheme.colorScheme.background))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = addMessage,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(horizontal = 36.dp)
            )
            Text(
                text = "+ $points " + stringResource(id = com.cmu.estg.cmu_geocaching.R.string.opinion_of_cache),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 50.dp)
            )

            Button(
                onClick = { navController.navigate("ratecache") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF65558F)
                )
            ) {
                Text(
                    text = stringResource(id = com.cmu.estg.cmu_geocaching.R.string.rate_trivia_answer),
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp),
                )
            }

        }
    }
}


