package com.example.stromkalkulator.ui.components.quizComponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(progress: Float) {
    LinearProgressIndicator(
        modifier = Modifier
            .semantics(mergeDescendants = true) {}
            .padding(10.dp),
        progress = progress,
    )
}