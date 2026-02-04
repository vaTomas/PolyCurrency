package ph.eece.polycurrency.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ph.eece.polycurrency.ui.calculator.CalculatorScreen
import ph.eece.polycurrency.ui.currency.CurrencySelectionScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "calculator"
    ) {
        // Calculator Screen
        composable("calculator") {
            CalculatorScreen(
                onOpenCurrencySelector = { navController.navigate("currency_selection") }
            )
        }

        // Currency Selection Screen
        composable("currency_selection") {
            CurrencySelectionScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}