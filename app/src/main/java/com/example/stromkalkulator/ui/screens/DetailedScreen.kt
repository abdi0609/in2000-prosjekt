package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stromkalkulator.data.models.Day
import com.example.stromkalkulator.data.repositories.ElectricityPrice


@Composable
fun DetailedView(innerPadding: PaddingValues) {

    val electricityPrice = ElectricityPrice()
    val hours = produceState(
        initialValue = Day(emptyList()),
        producer = { value = electricityPrice.getTomorrow() })

    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        items(hours.value.hours){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                //.background()
            ) {
                Text(text = it.NOK_per_kWh)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it.EUR_per_kWh)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it.EXR)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it.time_start)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it.time_end)

            }
        }
    }
}

