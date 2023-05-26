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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stromkalkulator.R
import com.example.stromkalkulator.ui.components.TopBar
import com.example.stromkalkulator.viewmodels.CalculatorViewModel
import java.math.RoundingMode

// Make card for activity
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoCard(
    icon: Int,
    standard: Int,
    maks: Int,
    electricityPrice: Double,
    objectCost: Double,
    steps: Int,
    stringResource: Int)
{
    // Make box for Card
    Box {
        var expanded by remember { mutableStateOf(false) }
        val rotationState by animateFloatAsState( targetValue = if (expanded) 180f else 0f )
        val sliderPointerFraction = (standard.toFloat()/maks)
        var pointerValue: Float by remember { mutableStateOf(sliderPointerFraction) }
        val calculatedPriceReal = { eprice: Double, ocost: Double, pval: Float ->
            (eprice * ocost) * pval / 60
        }

        // Create Card
        Card(
            // Card can expand if clicked on
            onClick = {expanded = !expanded},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            // Use column to add padding around the whole Row
            Column( modifier = Modifier.padding(16.dp) ) {
                Row(
                    Modifier.height(75.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        // Create shadow for activity icon
                        Icon(
                            modifier = Modifier
                                .fillMaxWidth(0.23F)
                                .padding(start = 10.dp)
                                .height(80.dp)
                                .offset(3.dp, 3.dp),
                            tint = Color(0, 0, 0, 30),
                            imageVector = ImageVector.vectorResource(icon),
                            contentDescription = stringResource(id = stringResource)
                        )
                        // Create activity icon
                        Icon(
                            modifier = Modifier
                                .fillMaxWidth(0.23F)
                                .padding(start = 10.dp)
                                .height(80.dp),
                            imageVector = ImageVector.vectorResource(icon),
                            contentDescription = ""
                        )
                    }
                    // Create box where text filling 80% of remaining room in Card
                    Box(
                        Modifier
                            .fillMaxWidth(0.8f)
                            .height(80.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "${"%.2f".format(
                                calculatedPriceReal(electricityPrice,objectCost,(pointerValue*maks))
                            )} KR i ${(pointerValue * maks).toInt()} min",
                            fontSize = 20.sp,
                        )
                    }
                    // Create iconbutton to extend Card
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
                // Expand card
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    // Create column for Text and Slider
                    Column (Modifier.padding(10.dp)) {
                        Text(
                            text = stringResource(R.string.adjust_interval),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier
                                .padding(top = 5.dp, end = 10.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        // Create slider for changing time-interval
                        Slider(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                            value = pointerValue,
                            onValueChange = { newValue -> pointerValue = newValue },
                            valueRange = 0f..1f,
                            steps = steps,
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    paddingValue: PaddingValues,
    viewModel: CalculatorViewModel = viewModel(factory = CalculatorViewModel.Factory),
) {
    // Create scaffold for calculator screen containing topbar and content
    Scaffold(
        topBar = { TopBar(viewModel, viewModel.calculatorStateFlow.collectAsState().value.region) },
        content = { CalculatorView(it, viewModel) },
        modifier = Modifier.padding(paddingValue)
    )
}

@ExperimentalMaterial3Api
@Composable
fun CalculatorView(
    paddingValue: PaddingValues,
    viewModel: CalculatorViewModel
) {

    val state = viewModel.calculatorStateFlow.collectAsState()

    val valueRange: ClosedFloatingPointRange<Float> = 0f..1f
    val defaultValue = (state.value.currentHour / 23.0).toFloat()
    var value: Float by remember { mutableStateOf(defaultValue) }
    val chosenElectricityPrice: Double = try {
        state.value.pricesToday[(value * 23.0).toBigDecimal()
            .setScale(1, RoundingMode.HALF_UP).toInt()]
    } catch (e: Exception) {
        0.00
    }

    Surface(Modifier.padding(paddingValue)) {
        Box(Modifier.fillMaxSize()) {
            Column (
                modifier = Modifier
                    // Place column in topcenter of box
                    .align(Alignment.TopCenter)
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                // Create card activity for showering
                InfoCard(
                    icon = R.drawable.shower_solid,
                    // Set 30 minutes as standard time-interval
                    standard = 30,
                    // Set 60 minutes as maxlimit
                    maks = 60,
                    electricityPrice = chosenElectricityPrice,
                    objectCost = 33.6,
                    // 2 minutes per step
                    steps = 30,
                    stringResource = R.string.shower_icon
                )
                // Create card activity for charging car
                InfoCard(
                    icon = R.drawable.charging_station_solid,
                    // Set 6 hours as standard time-interval
                    standard = 360,
                    // Set 24 hours as maxlimit
                    maks = 1440,
                    electricityPrice = chosenElectricityPrice,
                    objectCost = 1.0,
                    // 1 hour per step
                    steps = 24,
                    stringResource = R.string.car_charger_icon
                )
                // Create card activity for laundry machine
                InfoCard(
                    icon = R.drawable.baseline_local_laundry_service_24,
                    // Set 2 hours as standard time-interval
                    standard = 120,
                    // Set 6 hours as maxlimit
                    maks = 360,
                    electricityPrice = chosenElectricityPrice,
                    objectCost =  22.0,
                    // 30 minutes per step
                    steps = 12,
                    stringResource = R.string.laundry_machine_icon
                )
                // Create card activity for heater
                InfoCard(
                    icon = R.drawable.heater,
                    // Set 6 hours as standard time-interval
                    standard = 360,
                    // Set 24 hours as maxlimit
                    maks = 1440,
                    electricityPrice = chosenElectricityPrice,
                    objectCost = 1.0,
                    // 1 hour per step
                    steps = 24,
                    stringResource = R.string.heater_icon)
                Spacer(modifier = Modifier
                    .height(100.dp)
                    .width(30.dp))
            }
            // Create Card with slider for changing time of day
            Card(
                border = CardDefaults.outlinedCardBorder(false),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier
                    .padding(20.dp)
                    // Place in bottom of screen
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = stringResource(id = R.string.adjust_time_of_the_day),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .align(Alignment.CenterHorizontally)
                )
                // Display time of day
                Text(
                    text = "%02.0f".format(value * 23.0) + ":00",
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .align(Alignment.CenterHorizontally),
                )
                // Create slider for changing time of day
                Slider(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    value = value,
                    onValueChange = { newValue -> value = newValue },
                    valueRange = valueRange,
                    // Edges count as steps
                    // Set steps between edges
                    steps = 22,
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