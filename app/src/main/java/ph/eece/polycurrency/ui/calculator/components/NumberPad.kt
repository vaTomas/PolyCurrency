package ph.eece.polycurrency.ui.calculator.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ph.eece.polycurrency.ui.calculator.CalculatorEvent
import ph.eece.polycurrency.ui.components.CalculatorButton

@Composable
fun NumberPad(
    onEvent: (CalculatorEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Define Colors
    val operatorColor = MaterialTheme.colorScheme.secondaryContainer
    val operatorText = MaterialTheme.colorScheme.onSecondaryContainer
    val actionColor = Color(0xFFEF5350) // Red for AC
    val primaryColor = MaterialTheme.colorScheme.primary

    Column(modifier = modifier) {
        Row(Modifier.weight(1f)) {
            CalculatorButton("AC", onClick={ onEvent(CalculatorEvent.OnClear) }, modifier=Modifier.weight(1f), backgroundColor = actionColor, textColor = Color.White)
            CalculatorButton("( )", onClick={ /* TODO: Bracket Logic */ }, modifier=Modifier.weight(1f))
            CalculatorButton("%", onClick={ onEvent(CalculatorEvent.OnOperator('%')) }, modifier=Modifier.weight(1f))
            CalculatorButton("÷", onClick={ onEvent(CalculatorEvent.OnOperator('/')) }, modifier=Modifier.weight(1f), backgroundColor = operatorColor, textColor = operatorText)
        }

        Row(Modifier.weight(1f)) {
            CalculatorButton("7", onClick={ onEvent(CalculatorEvent.OnDigit('7')) }, modifier=Modifier.weight(1f))
            CalculatorButton("8", onClick={ onEvent(CalculatorEvent.OnDigit('8')) }, modifier=Modifier.weight(1f))
            CalculatorButton("9", onClick={ onEvent(CalculatorEvent.OnDigit('9')) }, modifier=Modifier.weight(1f))
            CalculatorButton("×", onClick={ onEvent(CalculatorEvent.OnOperator('*')) }, modifier=Modifier.weight(1f), backgroundColor = operatorColor, textColor = operatorText)
        }

        Row(Modifier.weight(1f)) {
            CalculatorButton("4", onClick={ onEvent(CalculatorEvent.OnDigit('4')) }, modifier=Modifier.weight(1f))
            CalculatorButton("5", onClick={ onEvent(CalculatorEvent.OnDigit('5')) }, modifier=Modifier.weight(1f))
            CalculatorButton("6", onClick={ onEvent(CalculatorEvent.OnDigit('6')) }, modifier=Modifier.weight(1f))
            CalculatorButton("-", onClick={ onEvent(CalculatorEvent.OnOperator('-')) }, modifier=Modifier.weight(1f), backgroundColor = operatorColor, textColor = operatorText)
        }

        Row(Modifier.weight(1f)) {
            CalculatorButton("1", onClick={ onEvent(CalculatorEvent.OnDigit('1')) }, modifier=Modifier.weight(1f))
            CalculatorButton("2", onClick={ onEvent(CalculatorEvent.OnDigit('2')) }, modifier=Modifier.weight(1f))
            CalculatorButton("3", onClick={ onEvent(CalculatorEvent.OnDigit('3')) }, modifier=Modifier.weight(1f))
            CalculatorButton("+", onClick={ onEvent(CalculatorEvent.OnOperator('+')) }, modifier=Modifier.weight(1f), backgroundColor = operatorColor, textColor = operatorText)
        }

        Row(Modifier.weight(1f)) {
            CalculatorButton("0", onClick={ onEvent(CalculatorEvent.OnDigit('0')) }, modifier=Modifier.weight(1f))
            CalculatorButton(".", onClick={ onEvent(CalculatorEvent.OnDigit('.')) }, modifier=Modifier.weight(1f))
            CalculatorButton("⌫", onClick={ onEvent(CalculatorEvent.OnDelete) }, modifier=Modifier.weight(1f))
            CalculatorButton("=", onClick={ onEvent(CalculatorEvent.OnEvaluate) }, modifier=Modifier.weight(1f), backgroundColor = primaryColor, textColor = Color.White)
        }
    }
}