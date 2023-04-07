package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stromkalkulator.ui.components.PriceTemperatureGraph
import com.example.stromkalkulator.ui.theme.*
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
        CurrentPriceBubble(priceString = currentPrice)
        PriceTemperatureGraph(temps = viewModel.getTemperatures(), prices = viewModel.getPrices())
        Spacer(modifier = Modifier.height(60.dp)) // FIXME: Space på bunnen her funker ikke
    }
}

// Card that shows current price
@Composable
private fun CurrentPriceBubble(priceString: String) {
    val fontSize = 25.sp
    val price = priceString.toDoubleOrNull()
    val priceText = "%.2f".format(price)

    val textColor =
        if (price == null) Red
        else if (price < 1.2) GreenTextContrast
        else if (price < 2) YellowTextContrast
        else RedTextContrast

    val backgroundColor =
        if (price == null) Red
        else if (price < 1.2) Green
        else if (price < 2) Yellow
        else Red

    Card (
        modifier = Modifier.size(200.dp, 200.dp),
        shape = CircleShape,
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "$priceText kr/kwh",
                color = textColor,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(PaddingValues(16.dp))
}