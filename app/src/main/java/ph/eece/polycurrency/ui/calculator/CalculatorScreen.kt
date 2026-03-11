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
import androidx.compose.material3.Scaffold
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
    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {

//            // Header
//            CalculatorHeader(
//                isHistoryOpen = state.isHistoryOpen,
//                onToggleHistory = { viewModel.onEvent(CalculatorEvent.OnToggleHistory) },
//                onMoreOptions = {}
//            )
//
//            // History
//            AnimatedVisibility(visible = state.isHistoryOpen) {
//                Box(Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//                    .background(MaterialTheme.colorScheme.surfaceVariant)
//                ) {
//                    Text("History coming soon...", Modifier.align(Alignment.Center))
//                }
//            }

            // Top Half
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                CalculatorDisplay(
                    expression = state.inputExpression,
                    result = state.liveResult,
                    targetCurrencyCode = state.targetCurrencyCode,
                    activeCurrencies = state.activeCurrencies,
                    onCurrencySelected = { selectedCode ->
                        viewModel.onEvent(CalculatorEvent.OnChangeTargetCurrency(selectedCode))
                    }
                )
            }

            // Bottom Half
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4.236f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                // Extras Toggle
//                IconButton(
//                    onClick = { viewModel.onEvent(CalculatorEvent.OnToggleExtras) },
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                ) {
//                    Icon(
//                        imageVector = if (state.isExtrasOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//                        contentDescription = "Toggle Extras",
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                }

                // Extras Keypad
                AnimatedVisibility(visible = state.isExtrasOpen) {
                    ExtrasPanel(
                        currencies = state.activeCurrencies,
                        onEvent = viewModel::onEvent,
                        onManageCurrencies = onOpenCurrencySelector
                    )
                }


//                // Input Editor
//                CalculatorInput(
//                    value = state.inputExpression,
//                    onValueChange = { /* TODO: Handle input changes */ },
//                    modifier = Modifier
//                        .weight(0.15f)
//                        .background(MaterialTheme.colorScheme.surfaceVariant)
//                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
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
    }
}