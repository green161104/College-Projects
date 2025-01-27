package com.cmu.estg.cmu_geocaching.data.remote.retrofit

data class TriviaQuestion(
    val category: String,
    val correctAnswer: String,
    val difficulty: String,
    val id: String,
    val incorrectAnswers: List<String>,
    val isNiche: Boolean,
    val question: Question,
    val regions: List<String>,
    val tags: List<String>,
    val type: String
)