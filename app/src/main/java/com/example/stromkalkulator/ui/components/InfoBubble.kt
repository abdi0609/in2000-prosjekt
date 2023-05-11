package com.example.stromkalkulator.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.stromkalkulator.R
import com.example.stromkalkulator.ui.theme.extendedColors

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
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.outline
        )
    }
    if (popupControl) {
        AlertDialog(
            onDismissRequest = { popupControl = false },
            title = { Text(stringResource(R.string.cur_price)) },
            text = { Text(buildAnnotatedString {
                append(stringResource(R.string.info_bubble) + "\n")
                withStyle(style = SpanStyle(color = extendedColors().green, fontWeight = FontWeight.ExtraBold)) {
                    append(stringResource(R.string.low) + "\n")
                }
                withStyle(style = SpanStyle(color = extendedColors().yellow, fontWeight = FontWeight.ExtraBold)) {
                    append(stringResource(R.string.medium) + "\n")
                }
                withStyle(style = SpanStyle(color = extendedColors().red, fontWeight = FontWeight.ExtraBold)) {
                    append(stringResource(R.string.high) + "\n")
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