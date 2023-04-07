package com.example.stromkalkulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stromkalkulator.data.models.electricity.Day
import com.example.stromkalkulator.data.repositories.ElectricityPrice
import com.example.stromkalkulator.viewmodels.DetailedViewModel
import com.example.stromkalkulator.viewmodels.MainViewModel


@Composable
fun DetailedView(innerPadding: PaddingValues, mainViewModel: MainViewModel) {
    val viewModel: DetailedViewModel = viewModel()

    val electricityPrice = ElectricityPrice(viewModel.httpClient)
    val hours = produceState(
        initialValue = Day(emptyList()),
        producer = {
            value = electricityPrice.getTomorrow(mainViewModel.mainStateFlow.value.region)
        })

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

