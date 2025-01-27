package com.cmu.estg.cmu_geocaching.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.UserCacheCrossRef
import com.cmu.estg.cmu_geocaching.data.local.repository.CacheRepository
import com.cmu.estg.cmu_geocaching.data.local.repository.UserCacheCrossRefRepository
import com.cmu.estg.cmu_geocaching.data.local.repository.UserRepository
import com.cmu.estg.cmu_geocaching.data.remote.retrofit.RetrofitInstance
import com.cmu.estg.cmu_geocaching.data.remote.retrofit.TriviaQuestion
import com.cmu.estg.cmu_geocaching.data.remote.retrofit.TriviaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class TriviaQuestionViewModel(
    application: Application,
    private val userCacheRepository: UserCacheCrossRefRepository,
    private val userRepository: UserRepository,
    private val cacheRepository: CacheRepository
) : AndroidViewModel(application) {

    private val triviaApi: RetrofitInstance = RetrofitInstance()
    private val triviaRepository: TriviaRepository = TriviaRepository(triviaApi.api)

    private var _isAnswerCorrect =
        MutableStateFlow<Boolean?>(null) // Null when no answer is selected
    val isAnswerCorrect: StateFlow<Boolean?> = _isAnswerCorrect

    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
    val questions: StateFlow<List<TriviaQuestion>> = _questions

    private val _selectedQuestion = MutableStateFlow<TriviaQuestion?>(null)
    val selectedQuestion: StateFlow<TriviaQuestion?> = _selectedQuestion

    fun fetchTriviaQuestions() {
        viewModelScope.launch {
            try {
                val result = triviaRepository.fetchQuestions()
                result.onSuccess { fetchedQuestions ->
                    if (fetchedQuestions.isEmpty()) {
                        Log.e("TriviaViewModel", "No questions found!")
                    } else {
                        _questions.value = fetchedQuestions
                        selectRandomQuestion()
                    }
                }.onFailure { error ->
                    Log.e("TriviaViewModel", "Error fetching trivia questions: ${error.message}")
                }
            } catch (exception: Exception) {
                // Handle any unexpected errors (e.g., network issues)
                Log.e("TriviaViewModel", "Unexpected error: ${exception.message}")
            }
        }
    }

    private fun selectRandomQuestion() {
        val currentQuestions = _questions.value
        if (currentQuestions.isNotEmpty()) {
            val randomQuestion = currentQuestions.random() // Select a random question
            _selectedQuestion.value = randomQuestion
        } else {
            Log.e("TriviaViewModel", "No questions to select from!")
        }
    }

    fun checkAnswer(selectedAnswer: String) {
        val question = _selectedQuestion.value
        if (question != null) {
            _isAnswerCorrect.value = selectedAnswer == question.correctAnswer
        }
    }

    fun resetAnswer() {
        _isAnswerCorrect.value = null
    }

    fun addUserPointsAndCacheHistory(cache: Cache) {
        viewModelScope.launch {
            val pointsEarned =
                if(isAnswerCorrect.value == true) {
                    150
                }else{
                    50
                }
            val currentUser = userRepository.getCachedUser();
            if (currentUser != null) {
                currentUser.points = (currentUser.points ?: 0) + pointsEarned

            userRepository.updateUserPoints(currentUser)
            val userCache = UserCacheCrossRef(
                currentUser.userId,
                cache.cacheId,
                Date(),
                pointsEarned = pointsEarned ,
            )
            userCacheRepository.upsertUserCacheCrossRef(userCache)
            cacheRepository.upsertCache(cache)

        }
            }

    }
}


class TriviaQuestionViewModelFactory(
    private val application: Application,
    private val userCacheRepository: UserCacheCrossRefRepository,
    private val userRepository: UserRepository,
    private val cacheRepository: CacheRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TriviaQuestionViewModel::class.java)) {
            Log.d("ViewModelFactory", "Creating TriviaQuestionViewModel")
            return TriviaQuestionViewModel(application, userCacheRepository, userRepository, cacheRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


