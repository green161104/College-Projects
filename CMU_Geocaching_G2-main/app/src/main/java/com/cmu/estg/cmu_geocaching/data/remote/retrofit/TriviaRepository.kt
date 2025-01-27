package com.cmu.estg.cmu_geocaching.data.remote.retrofit

class TriviaRepository(private val api: TriviaApi) {

    // Function to fetch trivia questions
    suspend fun fetchQuestions(): Result<List<TriviaQuestion>> {
        return try {
            val response = api.getQuestions()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error fetching questions: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}