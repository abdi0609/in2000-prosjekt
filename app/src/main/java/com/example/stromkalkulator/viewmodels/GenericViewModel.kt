package com.example.stromkalkulator.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.stromkalkulator.data.Region

abstract class GenericViewModel: ViewModel() {
    abstract fun setRegion(region: Region)
    abstract fun updateTempsAndPrices()
}