package com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cmu.estg.cmu_geocaching.R
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.AppButton
import com.cmu.estg.cmu_geocaching.viewModel.SharedCacheViewModel


@Preview(showBackground = true)
@Composable
fun RateYourExperienceScreenPreview() {
    val sharedCacheViewModel = viewModel<SharedCacheViewModel>()
    val navController = NavController(LocalContext.current)
    RateYourExperienceScreen(navController, sharedCacheViewModel)
}


@Composable
fun RateYourExperienceScreen(
    navController: NavController,
    sharedCacheViewModel: SharedCacheViewModel,
) {
    // State to hold user's choice
    val selectedCache by sharedCacheViewModel.selectedCache.collectAsState()
    Log.d("RATING SELECTED CACHE", selectedCache.toString())
    val feedback = remember { mutableStateOf<String?>(null) }

    // Main Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.opinion_of_cache),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Like and Dislike Buttons
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppButton(
                onClickAction = {
                    selectedCache?.let {
                        selectedCache?.rating?.let { rating ->
                            val newRating = minOf(rating + 1, 100)
                            sharedCacheViewModel.updateCacheRating(newRating)
                            feedback.value = "Like"
                        }
                    }
                },
                buttonText = stringResource(R.string.rate_like),
                icon = {
                    Icon(
                        Icons.Filled.ThumbUp,
                        contentDescription = "Like Icon",
                        tint = Color.White
                    )
                },
            )

            AppButton(
                onClickAction = {
                    selectedCache?.let {
                        selectedCache?.rating?.let { rating ->
                            // For "Dislike", decrement, but ensure it doesn't go below 0
                            val newRating = maxOf(rating - 1, 0)
                            sharedCacheViewModel.updateCacheRating(newRating)
                            feedback.value = "Dislike"
                        }
                    }
                },
                buttonText = stringResource(R.string.rate_dislike),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.icons8_thumbs_down_24),
                        contentDescription = "Thumbs Down Icon",
                        Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            )
        }

        // Display the feedback message
        feedback.value?.let {
            Text(
                text = stringResource(R.string.cache_feedback_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp)
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("homepage") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6456)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentSize()
                    .height(48.dp)
                    .border(3.dp, Color(0xFFB195DC), RoundedCornerShape(24.dp))
                    .shadow(2.dp, shape = RoundedCornerShape(24.dp)),
            ) {
                Text(
                    text = stringResource(R.string.return_to_homepage),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}