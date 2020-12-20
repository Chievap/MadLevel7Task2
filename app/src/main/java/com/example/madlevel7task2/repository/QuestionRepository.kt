package com.example.madlevel7task2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel7task2.model.Question
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class QuestionRepository {
    private val TAG = "QuestionRepository"

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = firestore.collection("questions")

    private val _questions: MutableLiveData<List<Question>> = MutableLiveData()
    val questions: LiveData<List<Question>>
        get() = _questions

    fun getQuestions() {
        collectionReference
            .get()
            .addOnSuccessListener { result ->
                val questionList = mutableListOf<Question>()
                for (document in result) {
                    val question: Question = document.toObject(Question::class.java)
                    questionList.add(question)
                    Log.d(TAG, question.toString())
                }
                _questions.value = questionList
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                throw Error("Retrieval of questions was unsuccessful!")
            }

    }
}