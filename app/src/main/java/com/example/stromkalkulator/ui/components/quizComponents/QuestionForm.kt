package com.example.stromkalkulator.ui.components.quizComponents

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuestionForm(quiz: Quiz, innerPadding: PaddingValues, showForm: MutableState<Boolean>) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val question = quiz.questions[currentQuestionIndex]

    val answer = remember{ mutableStateOf("") }

    // progress bar
    val progressPointsPerQuestion = 1 / quiz.questions.size.toFloat()
    var progress by remember { mutableStateOf(0f) }
    var progressBarMoved by remember { mutableStateOf(false) }

    // progress bar animation
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    Column(modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Column(modifier = Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            // question text
            Text(question.question, fontSize = 25.sp, fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

            // show answering alternatives
            question.alternatives.keys.forEach {
                // if selected button
                if (answer.value == it) {
                    Button(
                        onClick = {
                            // save answer for this question
                            answer.value = it
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(it)
                    }
                }
                // not selected button
                else {
                    Button(
                        onClick = {
                            // save answer for this question
                            answer.value = it
                            // increase progress bar if first alternative selected
                            if (!progressBarMoved) {
                                progress += progressPointsPerQuestion
                                progressBarMoved = true
                            }
                        }
                    ) {
                        Text(it)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Box(contentAlignment = Alignment.Center) {
            // next button
            TextButton(
                modifier = Modifier.offset(y = (-30).dp),
                enabled = progressBarMoved,
                onClick = {
                    // save answer
                    quiz.answer(currentQuestionIndex, answer.value)
                    // next question
                    if (currentQuestionIndex < quiz.questions.size - 1) {
                        currentQuestionIndex++
                    }
                    // submit form
                    else {
                        quiz.finished = true
                        showForm.value = false
                    }
                    // reset selected answer
                    answer.value = ""
                    // progress bar ready for new increase
                    progressBarMoved = false

                }) {
                // button says submit if last question, else next
                val buttonText = if (currentQuestionIndex < quiz.questions.size - 1) "Next" else "Submit"
                Text(buttonText, fontSize = 20.sp)
            }
            // progress bar
            ProgressBar(animatedProgress)
        }

    }
}