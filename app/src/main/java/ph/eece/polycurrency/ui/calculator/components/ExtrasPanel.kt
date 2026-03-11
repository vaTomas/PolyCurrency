package ph.eece.polycurrency.ui.calculator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ph.eece.polycurrency.ui.calculator.CalculatorEvent

@Composable
fun ExtrasPanel(
    currencies: List<String>,
    onEvent: (CalculatorEvent) -> Unit,
    onManageCurrencies: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {

        // Row 1: Currencies
        CurrencyPad(
            currencies = currencies,
            onEvent = onEvent,
            onManageCurrencies = onManageCurrencies,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ow 2: Custom Operations
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val ops = listOf('Δ', '√', '(', ')')
            ops.forEach { op ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50)) // Capsule
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .clickable { onEvent(CalculatorEvent.OnOperator(op)) } // <--- Trigger standard operator event
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = op.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}