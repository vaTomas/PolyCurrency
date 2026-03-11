package ph.eece.polycurrency.ui.calculator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.eece.polycurrency.ui.currency.getFlagEmojiForCurrency

@Composable
fun CalculatorDisplay(
    expression: String,
    result: String,
    targetCurrencyCode: String,
    activeCurrencies: List<String>,
    onCurrencySelected: (String) -> Unit
) {
    val currencySymbol = remember(targetCurrencyCode) {
        try {
            java.util.Currency.getInstance(targetCurrencyCode).symbol
        } catch (e: Exception) {
            targetCurrencyCode // Fallback to "AED", etc.
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Equation Display
        EquationDisplay(expression = expression)

        Spacer(modifier = Modifier.height(12.dp))

        // Result Display
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TargetCurrencySelector(
                targetCurrencyCode = targetCurrencyCode,
                activeCurrencies = activeCurrencies,
                onCurrencySelected = onCurrencySelected
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Value
            ResultDisplay(
                result = result,
                currencySymbol = currencySymbol,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun TargetCurrencySelector(
    targetCurrencyCode: String,
    activeCurrencies: List<String>,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.clickable { expanded = true } // Open menu
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dynamic Flag using our math helper
                Text(text = getFlagEmojiForCurrency(targetCurrencyCode), fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = targetCurrencyCode,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }

        // The Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .heightIn(max = 300.dp)
        ) {
            activeCurrencies.forEach { code ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(getFlagEmojiForCurrency(code), fontSize = 18.sp)
                            Spacer(Modifier.width(12.dp))
                            Text(code, style = MaterialTheme.typography.bodyLarge)
                        }
                    },
                    onClick = {
                        onCurrencySelected(code)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun EquationDisplay(expression: String) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = expression.ifEmpty { " " },
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.horizontalScroll(
                state = scrollState,
                reverseScrolling = true
            )
        )
    }
}

@Composable
fun ResultDisplay(
    result: String,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = result.ifEmpty { "$currencySymbol 0.00" },
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.horizontalScroll(
                state = scrollState,
                reverseScrolling = true
            )
        )
    }
}