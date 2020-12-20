package com.example.madlevel7task2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel7task2.R
import com.example.madlevel7task2.databinding.FragmentQuestionsBinding
import com.example.madlevel7task2.model.Answer
import com.example.madlevel7task2.model.Question
import com.example.madlevel7task2.vm.QuestionsViewModel

class QuestionsFragment : Fragment() {
    private val TAG = "QuestionsFragment"
    private lateinit var binding: FragmentQuestionsBinding
    private val viewModel: QuestionsViewModel by viewModels()

    private var questions: List<Question> = arrayListOf()
    private var currentQuestionPosition: Int = 0

    private var selectedIsTrue: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuestionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener { onConfirmClick() }

        viewModel.getQuestions()
        observeQuestions()
    }

    private fun onConfirmClick() {
        if (selectedIsTrue) {

            if (currentQuestionPosition == (questions.size - 1)) {
                Toast.makeText(activity, getString(R.string.quiz_completed), Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_QuestionsFragment_to_QuestFragment)
            } else {
                // go to the next position in the questionsList en setup the layout
                currentQuestionPosition++
                setupCurrentQuestionLayout()
            }

        } else {
            Toast.makeText(activity, getString((R.string.wrong_answer)), Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeQuestions() {
        viewModel.questions.observe(viewLifecycleOwner, {
            this.questions = it
            setupCurrentQuestionLayout()
        })

        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupCurrentQuestionLayout() {
        if (currentQuestionPosition < questions.size) {
            val currentQuestion = this.questions[currentQuestionPosition]
            binding.tvQuestionTitle.text = currentQuestion.title
            binding.tvQuestionPosition.text = getString(
                R.string.question_position,
                (currentQuestionPosition + 1).toString(),
                questions.size.toString()
            )

            binding.rgQuestionAnswers.removeAllViews() // refresh radiobuttonGroup layout
            for (answer in currentQuestion.answers) {
                createRadioButtonForAnswer(answer)
            }
        }
    }

    private fun createRadioButtonForAnswer(answer: Answer) {
        val radioButton = RadioButton(activity)
        radioButton.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        radioButton.id = radioButton.hashCode()
        radioButton.text = answer.answer
        binding.rgQuestionAnswers.addView(radioButton)

        radioButton.setOnClickListener {
            this.selectedIsTrue = answer.isCorrect
        }
    }
}