package ph.eece.polycurrency.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val textStyle = MaterialTheme.typography.headlineMedium.copy(
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.End
    )

    val scrollState = rememberScrollState()

    LaunchedEffect(value) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            modifier = Modifier.horizontalScroll(scrollState),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.None
            ),
            // Phantom Zero Input
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterEnd) {
                    if (value.isEmpty()) {
                        Text(
                            text = "0",
                            style = textStyle.copy(color = MaterialTheme.colorScheme.outlineVariant) // Grey color
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}