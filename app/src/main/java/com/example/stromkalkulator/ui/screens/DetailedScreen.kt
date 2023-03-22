package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DetailedView(innerPadding: PaddingValues) {
    Text(text = "Look at all those graphs", modifier = Modifier.padding(innerPadding))
}