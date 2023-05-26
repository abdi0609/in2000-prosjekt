package com.example.stromkalkulator.ui.components.quizComponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stromkalkulator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionForm(quiz: Quiz, innerPadding: PaddingValues, showForm: MutableState<Boolean>) {
    // index of current question in quiz.questions
    var currentQuestionIndex by remember { mutableStateOf(0) }
    // get current question
    val question = quiz.questions[currentQuestionIndex]

    // current selected answer
    var answer by remember{ mutableStateOf("") }
    var alternativeSelected by remember { mutableStateOf(false) }

    Box (contentAlignment = Alignment.Center, modifier = Modifier.padding(innerPadding)){
        Column(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxSize()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Card(
                modifier = Modifier.fillMaxWidth().height(100.dp).padding(bottom = 20.dp).align(Alignment.CenterHorizontally)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = question.question,
                        modifier = Modifier.padding(5.dp),
                        fontSize = 20.sp
                    )
                }
            }
            question.alternatives.keys.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { questionValue ->
                        // different color if this alternative is selected
                        val color =
                            if (answer == questionValue) CardDefaults.cardColors(MaterialTheme.colorScheme.secondary) else CardDefaults.cardColors(
                                MaterialTheme.colorScheme.primary
                            )

                        Card(
                            modifier = Modifier.padding(8.dp).size(150.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = color,
                            onClick = {
                                // save answer for this question
                                answer = questionValue
                                alternativeSelected = true
                            },
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = questionValue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp),
            contentAlignment = Alignment.Center

        ) {
            // next button
            TextButton(
                modifier = Modifier.offset(y = (-30).dp),
                enabled = alternativeSelected,
                onClick = {
                    // save answer
                    quiz.answer(currentQuestionIndex, answer)
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
                    answer = ""
                    // ready for new alternative to be selected
                    alternativeSelected = false
                }
            ) {
                // button says submit if last question, else next
                val buttonText = if (currentQuestionIndex < quiz.questions.size - 1)
                    stringResource(R.string.next)
                    else stringResource(R.string.submit)
                Text(buttonText, fontSize = 20.sp)
            }
        }
    }
}

