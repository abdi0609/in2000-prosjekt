package com.example.stromkalkulator

import android.os.Bundle
import androidx.compose.material3.Icon
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stromkalkulator.ui.screens.CalculatorScreen
import com.example.stromkalkulator.ui.screens.DetailedScreen
import com.example.stromkalkulator.ui.screens.HomeScreen
import com.example.stromkalkulator.ui.theme.StromKalkulatorTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StromKalkulatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainView(navController)
                }
            }
        }
    }
}


/**
 * A sealed class that represents the screens we have in the app: home, detailed and calculator.
 *
 * @property route The route name of the screen.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detailed : Screen("detailed")
    object Calculator : Screen("calculator")
}

/**
 * The main composable function that sets up the app layout with a Scaffold and NavHost.
 * It renders the Bottombar as the bottom bar in the Scaffold.
 *
 * @param navController The NavController used to handle navigation between screens.
 */
@Composable
fun MainView(navController: NavHostController) {

    Scaffold (
        bottomBar = { Bottombar(navController) }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) { HomeScreen(innerPadding) }
            composable(Screen.Detailed.route) { DetailedScreen(innerPadding) }
            composable(Screen.Calculator.route) { CalculatorScreen(innerPadding) }
        }
    }
}

/**
 * Data class representing an item in the bottom navigation bar.
 *
 * @property screen A reference to the Screen object associated with the item.
 * @property icon An ImageVector representing the icon of the item.
 * @property description A string description of the item, which serves as both a label and
 * content description for accessibility purposes.
 */
private data class ScreensItem(
    val screen: Screen,
    val icon: ImageVector,
    val description: String
)


/**
 * Composable function which renders the bottom navigation bar with the specified items.
 *
 * @param navController The NavController used to handle navigation between screens.
 */
@Composable
private fun Bottombar(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(Screen.Home as Screen) }
    val screens = listOf(
        ScreensItem(Screen.Home, Icons.Default.Home, stringResource(R.string.home_button_text)),
        ScreensItem(
            Screen.Detailed,
            ImageVector.vectorResource(R.drawable.baseline_assessment_24),
            stringResource(R.string.detailed_button_text)
        ),
        ScreensItem(
            Screen.Calculator,
            ImageVector.vectorResource(R.drawable.baseline_calculate_24),
            stringResource(R.string.calculator_button_text)
        ),
    )
    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem (
                icon = { Icon(screen.icon, screen.description) },
                label = { Text(screen.description) },
                selected = selectedItem == screen.screen,
                onClick = {
                    selectedItem = screen.screen
                    navController.navigate(screen.screen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    StromKalkulatorTheme {
        val navController = rememberNavController()
        MainView(navController)
    }
}