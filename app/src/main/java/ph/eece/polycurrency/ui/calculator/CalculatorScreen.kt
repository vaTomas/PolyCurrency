package ph.eece.polycurrency.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.RectangleShape
import ph.eece.polycurrency.ui.calculator.components.CalculatorDisplay
import ph.eece.polycurrency.ui.calculator.components.CalculatorHeader
import ph.eece.polycurrency.ui.calculator.components.ExtrasPanel
import ph.eece.polycurrency.ui.calculator.components.NumberPad

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = hiltViewModel(), // Auto-injects the VM
    onOpenCurrencySelector: () -> Unit
) {
    // Collect the UI State from the ViewModel
    val state by viewModel.state.collectAsState()

    val buttonShape = if (state.isExtrasOpen) CircleShape else CircleShape
    val buttonAspectRatio = if (state.isExtrasOpen) 1f else 1f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Header
        CalculatorHeader(
            isHistoryOpen = state.isHistoryOpen,
            onToggleHistory = { viewModel.onEvent(CalculatorEvent.OnToggleHistory) },
            onMoreOptions = {}
        )

        // History
        AnimatedVisibility(visible = state.isHistoryOpen) {
            Box(Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text("History coming soon...", Modifier.align(Alignment.Center))
            }
        }

        // Live Result
        Spacer(Modifier.weight(1f))
        CalculatorDisplay(
            expression = state.inputExpression,
            result = state.liveResult,
            targetCurrencyCode = state.targetCurrencyCode,
            onTargetClick = {
                // TODO Make searchable
                val next = if (state.targetCurrencyCode == "PHP") "USD" else "PHP"
                viewModel.onEvent(CalculatorEvent.OnChangeTargetCurrency(next))
            }
        )

        // Extras Toggle
        IconButton(
            onClick = { viewModel.onEvent(CalculatorEvent.OnToggleExtras) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = if (state.isExtrasOpen) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Toggle Extras",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Extras Keypad
        AnimatedVisibility(visible = state.isExtrasOpen) {
            ExtrasPanel(
                currencies = state.activeCurrencies,
                onCurrencyClick = { viewModel.onEvent(CalculatorEvent.OnCurrency(it)) },
                onOperationClick = { viewModel.onEvent(CalculatorEvent.OnOperator(it)) }
            )
        }

        Spacer(Modifier.height(8.dp))

        // Input Editor
        CalculatorInput(
            value = state.inputExpression,
            onValueChange = { /* TODO: Handle input changes */ },
            modifier = Modifier
                .weight(0.15f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            NumberPad(
                onEvent = viewModel::onEvent,
                buttonShape = buttonShape,       // <--- Dynamic Shape passed here
                buttonAspectRatio = buttonAspectRatio // <--- Dynamic Ratio passed here
            )
        }
    }
}