package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.stromkalkulator.ui.components.quizComponents.Question
import com.example.stromkalkulator.ui.components.quizComponents.QuestionForm
import com.example.stromkalkulator.ui.components.quizComponents.Quiz
import com.example.stromkalkulator.ui.components.quizComponents.TipsColumn

val questions = listOf(
    Question("Hvor mange personer bor i din husholdning?",
        linkedMapOf("1" to "", "2" to "", "3" to "", "4" to "", "5" to "", "6+" to "")
    ),
    Question("Har du EL-bil?",
        linkedMapOf("Ja" to "Lad EL-bilen din når strømprisen er lav.", "Nei" to "")
    ),
    Question("Hvor lenge dusjer du?",
        linkedMapOf(
            "0-5 min" to "",
            "5-10 min" to "",
            "10-20 min" to "Ta kortere dusjer",
            "20-30 min" to "Ta kortere dusjer",
            "30+ min" to "Ta kortere dusjer"
        )
    ),
    Question("Bruker du oppvaskmaskin?", linkedMapOf("Ja" to "", "Nei" to "")),
    Question("Bruker du vaskemaskin?", linkedMapOf("Ja" to "", "Nei" to ""))
)

val quiz = Quiz(questions, mutableListOf())

@Composable
fun DetailedScreen(innerPadding: PaddingValues) {
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
                    Text("Take the quiz")
                }
            }

    }
}