package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stromkalkulator.ui.components.InfoBubble
import com.example.stromkalkulator.ui.components.CurrentPriceBubble
import com.example.stromkalkulator.ui.components.PriceTemperatureGraph
import com.example.stromkalkulator.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    paddingValue: PaddingValues,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val currentPrice = viewModel.getCurrentPrice().collectAsState().value.currentPrice

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Spacer(modifier = Modifier.size(40.dp))
        Box(contentAlignment = Alignment.TopEnd) {
            CurrentPriceBubble(priceString = currentPrice)
            InfoBubble()
        }
        PriceTemperatureGraph(temps = viewModel.getTemperatures(), prices = viewModel.getPrices())
        Spacer(modifier = Modifier.height(60.dp)) // FIXME: Space på bunnen her funker ikke
    }
}





@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(PaddingValues(16.dp))
}