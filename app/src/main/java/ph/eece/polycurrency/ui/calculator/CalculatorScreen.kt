package ph.eece.polycurrency.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

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
        // History
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // History Button
            IconButton(onClick = { viewModel.onEvent(CalculatorEvent.OnToggleHistory) }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "History",
                    // Visual Feedback: Tint primary if open, default color if closed
                    tint = if (state.isHistoryOpen) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }

            // Three Dots
            IconButton(onClick = { /* Do nothing for now */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Live Result
        Column(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
//            Column(horizontalAlignment = Alignment.End) {
//                // Placeholder for History List
//                Text(
//                    text = "History Empty",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(8.dp))

            // The Live Result
            Text(
                text = state.liveResult.ifEmpty { "PHP 0.00" }, // TODO Make dynamic
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
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