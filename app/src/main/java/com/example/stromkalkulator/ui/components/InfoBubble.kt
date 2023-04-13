package com.example.stromkalkulator.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.stromkalkulator.ui.theme.Green
import com.example.stromkalkulator.ui.theme.Red
import com.example.stromkalkulator.ui.theme.Yellow

// shows info for CurrentPriceBubble
@Composable
fun InfoBubble() {
    var popupControl by remember { mutableStateOf(false) }
    IconButton(
        onClick = { popupControl = true },
        modifier = Modifier.size(30.dp)) {
        Icon(
            imageVector =  Icons.Default.Info,
            contentDescription = "info",
            modifier = Modifier.size(30.dp)
        )
    }
    if (popupControl) {
        AlertDialog(
            onDismissRequest = { popupControl = false },
            title = { Text("Current Price") },
            text = { Text(buildAnnotatedString {
                append("Displays the average kr/kwh price for the current hour." +
                        "\nThe color changes based on the price:\n")
                withStyle(style = SpanStyle(color = Green, fontWeight = FontWeight.ExtraBold)) {
                    append("Low\n")
                }
                withStyle(style = SpanStyle(color = Yellow, fontWeight = FontWeight.ExtraBold)) {
                    append("Medium\n")
                }
                withStyle(style = SpanStyle(color = Red, fontWeight = FontWeight.ExtraBold)) {
                    append("High")
                }
            })
            },
            confirmButton = {
                Button(
                    onClick = { popupControl = false }
                ) {
                    Text("OK")
                }
            }
        )
    }

}