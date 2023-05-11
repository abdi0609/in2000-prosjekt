package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.stromkalkulator.R
import com.example.stromkalkulator.ui.components.quizComponents.Question
import com.example.stromkalkulator.ui.components.quizComponents.QuestionForm
import com.example.stromkalkulator.ui.components.quizComponents.Quiz
import com.example.stromkalkulator.ui.components.quizComponents.TipsColumn

@Composable
fun DetailedScreen(innerPadding: PaddingValues) {
    val questions = listOf(
        Question(
            stringResource(R.string.spm1),
            linkedMapOf("1" to stringResource(R.string.tip1_1),
                "2" to stringResource(R.string.tip1_2),
                "3" to stringResource(R.string.tip1_2),
                "4" to stringResource(R.string.tip1_2),
                "5" to stringResource(R.string.tip1_2),
                "6+" to stringResource(R.string.tip1_2))
        ),
        Question(
            stringResource(R.string.spm2),
            linkedMapOf(stringResource(R.string.yes) to stringResource(R.string.tip2_1),
                stringResource(R.string.no) to "")
        ),
        Question(stringResource(R.string.spm3),
            linkedMapOf(
                "0-5 min" to "",
                "5-10 min" to "",
                "10-20 min" to stringResource(R.string.tip3_2),
                "20-30 min" to stringResource(R.string.tip3_2),
                "30+ min" to stringResource(R.string.tip3_2)
            )
        ),
        Question(stringResource(R.string.spm4),
            linkedMapOf(stringResource(R.string.yes) to stringResource(R.string.tip4_1),
                stringResource(R.string.no) to "")),
        Question(stringResource(R.string.spm5),
            linkedMapOf(stringResource(R.string.yes) to stringResource(R.string.tip5_1),
                stringResource(R.string.no) to ""))
    )
    val quiz by remember{mutableStateOf(Quiz(questions, mutableListOf()))}
    val showQuestionForm = rememberSaveable { mutableStateOf(false) }

    // show question form
    if (showQuestionForm.value)
        QuestionForm(quiz, innerPadding, showQuestionForm)
    else {
        // show tips if user has done the quiz
        if (quiz.finished)
            TipsColumn(quiz, innerPadding)
        // show "take quiz" button
        else
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                Button(modifier = Modifier.padding(innerPadding), onClick = {
                    showQuestionForm.value = true
                }) {
                    Text(stringResource(R.string.take_quiz))
                }
            }

    }
}