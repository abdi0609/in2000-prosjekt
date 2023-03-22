package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(paddingValue: PaddingValues) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Spacer(modifier = Modifier.size(40.dp))
        Card (
            modifier = Modifier.size(250.dp, 250.dp),
            shape = CircleShape,
        ) {
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Current Price")
            }
        }

        Card(modifier = Modifier.size(300.dp, 300.dp)) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "I am a graph")
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(PaddingValues(16.dp))
}