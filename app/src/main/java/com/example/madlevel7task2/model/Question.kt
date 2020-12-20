package com.example.madlevel7task2.model

data class Question(
    var title: String = "",
    var answers: List<Answer> = arrayListOf()
)