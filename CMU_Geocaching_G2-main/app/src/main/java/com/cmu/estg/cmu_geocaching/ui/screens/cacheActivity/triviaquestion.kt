package com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity


import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cmu.estg.cmu_geocaching.ui.theme.CMU_GeocachingTheme
import com.cmu.estg.cmu_geocaching.viewModel.TriviaQuestionViewModel


@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    CMU_GeocachingTheme {
        val navcontroller = rememberNavController()
        val triviaViewModel: TriviaQuestionViewModel = viewModel()
        TriviaQuestionScreen(triviaViewModel, navcontroller)
    }
}

@Composable
fun TriviaQuestionScreen(triviaViewModel: TriviaQuestionViewModel, navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = remember { configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }

    // Fetch trivia questions
    LaunchedEffect(Unit) {
        triviaViewModel.fetchTriviaQuestions()
    }

    // Observing trivia data
    val question = triviaViewModel.selectedQuestion.collectAsState().value
    val correct = triviaViewModel.isAnswerCorrect.collectAsState().value
    var selectedQuestion by remember { mutableStateOf("") }

    // Handle loading state
    if (question == null) {
        // If no question is loaded yet, show a loading indicator
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background)))
                .padding(top = if (isLandscape) 40.dp else 20.dp)
        ) {
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = null,
                modifier = Modifier
                    .size(if (isLandscape) 100.dp else 120.dp)
                    .padding(vertical = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = stringResource(id = com.cmu.estg.cmu_geocaching.R.string.found_it),
                fontSize = if (isLandscape) 20.sp else 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp)
            )

            Question(
                question = question.question.text,
                options = question.incorrectAnswers.plusElement(question.correctAnswer),
                onOptionSelected = { selectedQuestion = it },
                triviaQuestionViewModel = triviaViewModel,
                selectedAnswer = selectedQuestion
            )


            LaunchedEffect(navController) {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    if (destination.route == "findcache") {
                        triviaViewModel.resetAnswer()
                    }
                } //clears the state, invisible to user (otherwise the change is too quick before the answer screen is gone)
            }

            LaunchedEffect(correct) {
                if (correct != null) {
                    navController.navigate("triviaanswer") {
                        launchSingleTop = true
                    }
                }
            }


        }
    }
}

@Composable
fun Question(
    question: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    triviaQuestionViewModel: TriviaQuestionViewModel,
    selectedAnswer: String
) {
    val configuration = LocalConfiguration.current
    val isLandscape = remember { configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }
    val shuffledOptions = options.shuffled() // Shuffles the options, including the correct one
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question,
            fontSize = if (isLandscape) 16.sp else 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = if (isLandscape) 16.dp else 36.dp)
        )

        Box {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Answers",
                    modifier = Modifier.size(if (isLandscape) 25.dp else 30.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.shuffled().forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                            Log.d(
                                "Answer is correct: ",
                                triviaQuestionViewModel.isAnswerCorrect.value.toString()
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(if (isLandscape) 10.dp else 110.dp))

        Button(
            onClick = { triviaQuestionViewModel.checkAnswer(selectedAnswer) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF65558F)
            )
        ) {
            Text(
                text = stringResource(id = com.cmu.estg.cmu_geocaching.R.string.submit),
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp),
            )
        }
    }
}
