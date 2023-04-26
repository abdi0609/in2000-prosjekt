package com.example.stromkalkulator.domain

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.stromkalkulator.data.Region
import kotlinx.coroutines.flow.*

// TODO: Move to Region enum as companion object?
class RegionRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val REGION = stringPreferencesKey("region")
    }

    private val region = dataStore.data.catch { it.printStackTrace() }.map { preferences ->
        enumValueOf<Region>(preferences[REGION] ?: "NO1")
    }

    suspend fun setRegion(region: Region) {
        Log.v("RegionRepository", "Setting region to: $region")
        dataStore.edit { preferences ->
            preferences[REGION] = region.toString()
        }
    }

    suspend fun getRegion(): Flow<Region> {
        Log.v("RegionRepository", "Getting region ${region.firstOrNull()}")
        return region
    }

}
//    private var region: Region = Region.NO1