package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stromkalkulator.ui.components.*
import com.example.stromkalkulator.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    paddingValue: PaddingValues,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
) {
    Scaffold(
        topBar = { TopBar(viewModel, viewModel.homeStateFlow.collectAsState().value.region) },
        content = { MainView(it, viewModel) },
        modifier = Modifier.padding(paddingValue)
    )
        
}

@Composable
private fun MainView(
    paddingValue: PaddingValues,
    viewModel: HomeViewModel
) {
    val state = viewModel.homeStateFlow.collectAsState()
    val currentPrice = state.value.currentPrice // current electricity price

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Spacer(modifier = Modifier.size(40.dp))
        // price bubble and info bubble
        Box(contentAlignment = Alignment.TopEnd) {
            CurrentPriceBubble(price = currentPrice)
            InfoBubble()
        }
        // graph
        PriceTemperatureGraph(viewModel)
        Spacer(modifier = Modifier.height(60.dp)) // FIXME: Space på bunnen her funker ikke
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(PaddingValues(16.dp))
}