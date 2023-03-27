package com.example.stromkalkulator.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stromkalkulator.data.models.Day
import com.example.stromkalkulator.data.models.HourPrice
import com.example.stromkalkulator.data.models.Week
import com.example.stromkalkulator.data.repositories.ElectricityPrice


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailedView(innerPadding: PaddingValues) {

    val electricityPrice = ElectricityPrice()
    val hours = produceState<Day>(
        initialValue = Day(emptyList()),
        producer = { value = electricityPrice.getTomorrow() })

    LazyColumn(
        modifier = Modifier.padding(PaddingValues())
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


 /*

    //Not working
    val electricityPrice = ElectricityPrice()
    val hours = produceState<Week>(
        initialValue = Week(emptyList<Day>().toMutableList()),
        producer = { value = electricityPrice.getWeek() }
    )

    LazyColumn(modifier = Modifier.padding(PaddingValues())) {
        items(hours.value.days) { hourPrices ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, top = 30.dp)
            ) {
                var i = 0
                hourPrices.hours.forEach { _ ->
                    Text(text = "Stromtime ${i}")
                    i++
                /*
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = it.NOK_per_kWh.toString())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = it.EUR_per_kWh.toString())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = it.EXR.toString())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = it.time_start.toString())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = it.time_end.toString())

                     */
                }
            }
        }
    }




  */

}

