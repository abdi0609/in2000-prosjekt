package com.example.stromkalkulator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stromkalkulator.ui.theme.*

// Card that shows current price
@Composable
fun CurrentPriceBubble(price: Double) {
    val fontSize = 35.sp
    //val price = priceString.toDoubleOrNull()
    val priceText = "%.2f".format(price)

    val textColor =
        if (price == null) extendedColors().onRed
        else if (price < 1.2) extendedColors().onGreen
        else if (price < 2) extendedColors().onYellow
        else extendedColors().onRed

    val backgroundColor =
        if (price == null) extendedColors().red
        else if (price < 1.2) extendedColors().green
        else if (price < 2) extendedColors().yellow
        else extendedColors().red

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
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(color = textColor, fontSize = fontSize,
                    fontWeight = FontWeight.Bold)) {
                    append(priceText)
                }
                withStyle(style = SpanStyle(color = textColor, fontSize = 18.sp)) {
                    append("\nkr/kwh")
                }
            }, textAlign = TextAlign.Center)
        }
    }
}