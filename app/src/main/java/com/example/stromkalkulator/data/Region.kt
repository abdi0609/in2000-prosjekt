package com.example.stromkalkulator.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.stromkalkulator.R
import java.util.concurrent.Flow


enum class Region(val stringId: Int) {
    NO1(R.string.region_east),
    NO2(R.string.region_south),
    NO3(R.string.region_middle),
    NO4(R.string.region_north),
    NO5(R.string.region_west);
}
