package ph.eece.polycurrency.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ph.eece.polycurrency.ui.components.CalculatorInput
import ph.eece.polycurrency.ui.calculator.CalculatorKeypad

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = hiltViewModel(), // Auto-injects the VM
    onOpenCurrencySelector: () -> Unit
) {
    // Collect the UI State from the ViewModel
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // History & Live Result
        Box(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                // Placeholder for History List
                Text(
                    text = "History Empty",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // The Live Result
                Text(
                    text = state.liveResult.ifEmpty { "≈ 0.00 PHP" },
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Input Editor
        CalculatorInput(
            value = state.inputExpression,
            onValueChange = { /* TODO: Handle input changes */ },
            modifier = Modifier
                .weight(0.15f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        // Keypad
        CalculatorKeypad(
            onEvent = viewModel::onEvent,
            activeCurrencies = state.activeCurrencies,
            onManageCurrencies = onOpenCurrencySelector,
            modifier = Modifier.weight(0.5f)
        )
    }
}