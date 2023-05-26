package com.example.stromkalkulator.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.stromkalkulator.R
import com.example.stromkalkulator.data.Region
import com.example.stromkalkulator.viewmodels.GenericViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: GenericViewModel, region: Region) {
    var expanded by remember { mutableStateOf(false) }
    // Create topbar
    TopAppBar(
        // Show region
        title = { Text( text = stringResource( id = region.stringId )) },
        // Create clickable dropdownmenu
        actions = {
            IconButton(
                content = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_location_24),
                        contentDescription = "edit_location" // TODO: Replace with string resource
                    )
                },
                onClick = { expanded = !expanded },
            )
            // Dropdown for of regions
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // Update temps and prices onClick
                Region.values().forEach { region ->
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = region.stringId)) },
                        onClick = {
                            viewModel.setRegion(region)
                            viewModel.updateTempsAndPrices()
                            expanded = false
                        }
                    )
                }
            }
        }
    )
}