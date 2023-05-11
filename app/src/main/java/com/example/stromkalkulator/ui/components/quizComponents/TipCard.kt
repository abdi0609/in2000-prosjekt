package com.example.stromkalkulator.ui.components.quizComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TipCard(tip: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
        .padding(20.dp))
        {
        Text(tip, fontSize = 20.sp, modifier = Modifier.padding(5.dp))
    }
}