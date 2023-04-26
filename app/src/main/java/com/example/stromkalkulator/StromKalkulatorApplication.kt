package com.example.stromkalkulator

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.stromkalkulator.domain.RegionRepository
import dagger.hilt.android.HiltAndroidApp

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@HiltAndroidApp
class StromKalkulatorApplication: Application() {
    lateinit var regionRepository: RegionRepository

    override fun onCreate() {
        super.onCreate()
        regionRepository = RegionRepository(dataStore)
    }

}
