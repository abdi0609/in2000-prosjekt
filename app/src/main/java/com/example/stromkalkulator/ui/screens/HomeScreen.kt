package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stromkalkulator.data.repositories.ElectricityPrice
import com.example.stromkalkulator.ui.theme.*
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun HomeScreen(paddingValue: PaddingValues) {
    val electricityPrice = ElectricityPrice()
    val current = produceState<String>(
        initialValue = "0.0",
        producer = { value = electricityPrice.getCurrent() })

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Spacer(modifier = Modifier.size(40.dp))
        CurrentPriceBubble(priceString = current.value)

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

// Card that shows current price
@Composable
fun CurrentPriceBubble(priceString: String) {
    val price = priceString.toDouble()
    val fontSize = 25.sp

    // 2 decimals
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.HALF_UP

    val textColor =
        // if low price
        if (price < 1.2) GreenTextContrast
        // average price
        else if (price < 2) YellowTextContrast
        // high price
        else RedTextContrast

    val backgroundColor =
        // if low price
        if (price < 1.2) Green
        // average price
        else if (price < 2) Yellow
        // high price
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
            Text(text = "${df.format(price)} kr/kwh", color = textColor, fontSize = fontSize,
                fontWeight = FontWeight.Bold)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(PaddingValues(16.dp))
}