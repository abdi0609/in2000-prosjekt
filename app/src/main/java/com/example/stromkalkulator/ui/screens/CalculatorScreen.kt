package com.example.stromkalkulator.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stromkalkulator.R
import com.example.stromkalkulator.viewmodels.DetailedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoCard(icon: Int, price: String) {
    Box() {
        var expanded by remember { mutableStateOf(false) }
        val rotationState by animateFloatAsState( targetValue = if (expanded) 180f else 0f )
        Card(
            onClick = {expanded = !expanded},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    Modifier.height(75.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box() {
                        Icon(
                            modifier = Modifier
                                .fillMaxWidth(0.23F)
                                .padding(start = 10.dp)
                                .height(80.dp)
                                .offset(3.dp, 3.dp), tint = Color(0, 0, 0, 30),
                            imageVector = ImageVector.vectorResource(icon),
                            contentDescription = "edit_location", // TODO: Replace with string resource
                        )
                        Icon(
                            modifier = Modifier
                                .fillMaxWidth(0.23F)
                                .padding(start = 10.dp)
                                .height(80.dp),
                            imageVector = ImageVector.vectorResource(icon),
                            contentDescription = "edit_location", // TODO: Replace with string resource
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxWidth(0.8f)
                            .height(80.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "$price KR i timen",
                            fontSize = 20.sp,
                        )
                    }
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.rotate(rotationState)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                }
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(text = "Her skal tidsintervall endres fra standard")
                }
            }
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun CalculatorScreen(innerPadding: PaddingValues) {
    val viewModel: DetailedViewModel = viewModel()
    val valueRange: ClosedFloatingPointRange<Float> = 0f..1f
    var value by remember { mutableStateOf(0f) }

    Surface(Modifier.padding(innerPadding)) {
        Box(Modifier.fillMaxSize()) {
            Column (
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                InfoCard(icon = R.drawable.shower_solid, price = "100")
                InfoCard(icon = R.drawable.charging_station_solid, price = "100")
                InfoCard(icon = R.drawable.baseline_local_laundry_service_24, price = "100")
                InfoCard(icon = R.drawable.heater, price = "100")
                Spacer(modifier = Modifier.height(80.dp).width(30.dp))
            }
            Card(
                border = CardDefaults.outlinedCardBorder(false),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(text = "Juster tidspunkt på dagen",
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 5.dp))
                Slider(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    value = value,
                    onValueChange = { newValue -> value = newValue },
                    valueRange = valueRange,
                    steps = 24,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.Gray,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }
    }
}