package ph.eece.polycurrency.ui.calculator.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ph.eece.polycurrency.ui.calculator.CalculatorEvent
import ph.eece.polycurrency.ui.components.CalculatorButton

@Composable
fun NumberPad(
    onEvent: (CalculatorEvent) -> Unit,
    buttonShape: Shape,
    buttonAspectRatio: Float,
    modifier: Modifier = Modifier
) {
    // Define Colors
    val operatorColor = MaterialTheme.colorScheme.secondaryContainer
    val operatorText = MaterialTheme.colorScheme.onSecondaryContainer
    val actionColor = MaterialTheme.colorScheme.errorContainer
    val actionText = MaterialTheme.colorScheme.onErrorContainer
    val primaryColor = MaterialTheme.colorScheme.primary
    val primaryText = MaterialTheme.colorScheme.onPrimary


    @Composable
    fun RowScope.PadButton(
        text: String,
        onClick: () -> Unit,
        color: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
        textColor: Color = MaterialTheme.colorScheme.onSurface,
        weight: Float = 1f,
        shape: Shape = buttonShape
    ) {
        CalculatorButton(
            symbol = text,
            onClick = onClick,
            modifier = Modifier
                .weight(weight)
                .aspectRatio(buttonAspectRatio),
            backgroundColor = color,
            textColor = textColor,
            shape = shape
        )
    }

    Column( modifier = modifier.fillMaxWidth() )
    {
        Row(Modifier.weight(1f)) {
            PadButton("AC", { onEvent(CalculatorEvent.OnClear) }, actionColor, actionText, weight = 1f)
            PadButton("( )", { onEvent(CalculatorEvent.OnSmartParenthesis) }, weight = 1f)
            PadButton("÷", { onEvent(CalculatorEvent.OnOperator('/')) }, operatorColor, operatorText, weight = 1f)
            PadButton("%", { onEvent(CalculatorEvent.OnPercent) }, operatorColor, operatorText, weight = 1f)
        }

        Row(Modifier.weight(1f)) {
            PadButton("7", { onEvent(CalculatorEvent.OnDigit('7')) }, weight = 1f)
            PadButton("8", { onEvent(CalculatorEvent.OnDigit('8')) }, weight = 1f)
            PadButton("9", { onEvent(CalculatorEvent.OnDigit('9')) }, weight = 1f)
            PadButton("×", { onEvent(CalculatorEvent.OnOperator('*')) }, operatorColor, operatorText, weight = 1f)
        }

        Row(Modifier.weight(1f)) {
            PadButton("4", { onEvent(CalculatorEvent.OnDigit('4')) }, weight = 1f)
            PadButton("5", { onEvent(CalculatorEvent.OnDigit('5')) }, weight = 1f)
            PadButton("6", { onEvent(CalculatorEvent.OnDigit('6')) }, weight = 1f)
            PadButton("-", { onEvent(CalculatorEvent.OnOperator('-')) }, operatorColor, operatorText, weight = 1f)
        }

        Row(Modifier.weight(1f)) {
            PadButton("1", { onEvent(CalculatorEvent.OnDigit('1')) }, weight = 1f)
            PadButton("2", { onEvent(CalculatorEvent.OnDigit('2')) }, weight = 1f)
            PadButton("3", { onEvent(CalculatorEvent.OnDigit('3')) }, weight = 1f)
            PadButton("+", { onEvent(CalculatorEvent.OnOperator('+')) }, operatorColor, operatorText, weight = 1f)
        }

        Row(Modifier.weight(1f)) {
            PadButton("0", { onEvent(CalculatorEvent.OnDigit('0')) }, weight = 1f)
            PadButton(".", { onEvent(CalculatorEvent.OnDigit('.')) }, weight = 1f)
            PadButton("⌫", { onEvent(CalculatorEvent.OnDelete) }, weight = 1f)
            PadButton("=", { onEvent(CalculatorEvent.OnEvaluate) }, primaryColor, primaryText, weight = 1f)
        }
    }
}