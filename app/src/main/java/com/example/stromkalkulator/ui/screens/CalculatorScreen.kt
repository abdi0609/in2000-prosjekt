package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CalculatorScreen(innerPadding: PaddingValues) {
    Text(text = "I am a calculator!!!", modifier = Modifier.padding(innerPadding))
}