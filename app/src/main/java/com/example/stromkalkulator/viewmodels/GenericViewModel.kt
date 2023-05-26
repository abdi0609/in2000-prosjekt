package com.example.stromkalkulator.viewmodels

import androidx.lifecycle.ViewModel
import com.example.stromkalkulator.data.Region

/**
 * An abstract class that all ViewModels in this app extend to ensure that they have the
 * setRegion() and updateTempsAndPrices() methods.
 */
abstract class GenericViewModel: ViewModel() {
    abstract fun setRegion(region: Region)
    abstract fun updateTempsAndPrices()
}