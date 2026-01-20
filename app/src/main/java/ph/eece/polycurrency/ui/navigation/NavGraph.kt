package ph.eece.polycurrency.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ph.eece.polycurrency.ui.calculator.CalculatorScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "calculator"
    ) {
        composable("calculator") {
            CalculatorScreen()
        }

        // TODO: Add "rates_settings" later
    }
}