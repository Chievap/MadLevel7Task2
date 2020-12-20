package com.example.madlevel7task2.model

import com.google.firebase.firestore.PropertyName

data class Answer(
    var answer: String = "",
    @get:PropertyName("isCorrect") var isCorrect: Boolean = false
)