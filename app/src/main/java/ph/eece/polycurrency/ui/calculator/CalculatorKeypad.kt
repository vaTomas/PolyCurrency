package ph.eece.polycurrency.ui.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ph.eece.polycurrency.ui.components.CalculatorButton

@Composable
fun CalculatorKeypad(
    onEvent: (CalculatorEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        Row(Modifier.weight(1f)) {
            CalculatorButton("AC", Modifier.weight(1f), backgroundColor = Color(0xFFEF5350)) { onEvent(CalculatorEvent.OnClear) }
            CalculatorButton("USD", Modifier.weight(1f), textColor = Color(0xFF4CAF50)) { onEvent(CalculatorEvent.OnCurrency("USD")) }
            CalculatorButton("PHP", Modifier.weight(1f), textColor = Color(0xFF4CAF50)) { onEvent(CalculatorEvent.OnCurrency("PHP")) }
            CalculatorButton("÷", Modifier.weight(1f), backgroundColor = MaterialTheme.colorScheme.secondaryContainer) { onEvent(CalculatorEvent.OnOperator('/')) }
        }

        Row(Modifier.weight(1f)) {
            CalculatorButton("7", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('7')) }
            CalculatorButton("8", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('8')) }
            CalculatorButton("9", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('9')) }
            CalculatorButton("×", Modifier.weight(1f), backgroundColor = MaterialTheme.colorScheme.secondaryContainer) { onEvent(CalculatorEvent.OnOperator('*')) }
        }

        Row(Modifier.weight(1f)) {
            CalculatorButton("4", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('4')) }
            CalculatorButton("5", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('5')) }
            CalculatorButton("6", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('6')) }
            CalculatorButton("-", Modifier.weight(1f), backgroundColor = MaterialTheme.colorScheme.secondaryContainer) { onEvent(CalculatorEvent.OnOperator('-')) }
        }

        Row(Modifier.weight(1f)) {
            CalculatorButton("1", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('1')) }
            CalculatorButton("2", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('2')) }
            CalculatorButton("3", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('3')) }
            CalculatorButton("+", Modifier.weight(1f), backgroundColor = MaterialTheme.colorScheme.secondaryContainer) { onEvent(CalculatorEvent.OnOperator('+')) }
        }

        Row(Modifier.weight(1f)) {
            CalculatorButton("0", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('0')) }
            CalculatorButton(".", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDigit('.')) }
            CalculatorButton("⌫", Modifier.weight(1f)) { onEvent(CalculatorEvent.OnDelete) }
            CalculatorButton("=", Modifier.weight(1f), backgroundColor = MaterialTheme.colorScheme.primary) { onEvent(CalculatorEvent.OnEvaluate) }
        }
    }
}