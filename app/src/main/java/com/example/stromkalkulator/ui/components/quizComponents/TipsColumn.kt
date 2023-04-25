package com.example.stromkalkulator.ui.components.quizComponents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TipsColumn(quiz: Quiz, innerPadding: PaddingValues) {
    LazyColumn(modifier = Modifier.padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally) {
        // title
        item {
            Text("Tips", fontWeight = FontWeight.Bold, fontSize = 40.sp)
        }
        // tips
        itemsIndexed(quiz.questions) {index, _ ->
            val tip = quiz.getTip(index)

            // if tip is available, show tip
            if (tip != null && tip != "") {
                TipCard(tip)
                Spacer(modifier = Modifier.height(20.dp))
            }

        }
    }
}