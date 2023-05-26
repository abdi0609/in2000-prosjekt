package com.example.stromkalkulator.domain

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.stromkalkulator.data.Region
import kotlinx.coroutines.flow.*

/**
 * This class is responsible for storing and retrieving the region from the DataStore.
 *
 * @param dataStore The DataStore to use.
 */
class RegionDomain(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val REGION = stringPreferencesKey("region")
    }

    private val region = dataStore.data.catch { it.printStackTrace() }.map { preferences ->
        enumValueOf<Region>(preferences[REGION] ?: "NO1")
    }

    /**
     * Sets the region in the DataStore.
     *
     * @param region The region to set.
     */
    suspend fun setRegion(region: Region) {
        Log.v("RegionRepository", "Setting region to: $region")
        dataStore.edit { preferences ->
            preferences[REGION] = region.toString()
        }
    }

    /**
     * Gets the region from the DataStore.
     *
     * @return The region.
     */
    suspend fun getRegion(): Flow<Region> {
        Log.v("RegionRepository", "Getting region ${region.firstOrNull()}")
        return region
    }

}
//    private var region: Region = Region.NO1