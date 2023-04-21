package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.ViewModel
import com.example.stromkalkulator.data.repositories.ElectricityPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//@HiltViewModel
//@Deprecated("Bad")
//class MainViewModel @Inject constructor(
//    private val sharedViewModel: SharedViewModel
//): ViewModel() {
//    fun setRegion(region: ElectricityPrice.Region) {
//        sharedViewModel.setRegion(region)
//    }
//
//    fun updateTempsAndPrices() {
//        sharedViewModel.updateTempsAndPrices()
//    }
//
//    fun getRegion() = sharedViewModel.sharedStateFlow
//}