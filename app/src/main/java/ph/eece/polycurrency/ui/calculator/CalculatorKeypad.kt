package ph.eece.polycurrency.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import ph.eece.polycurrency.ui.calculator.components.CurrencyPad
import ph.eece.polycurrency.ui.calculator.components.NumberPad

@Composable
fun CalculatorKeypad(
    onEvent: (CalculatorEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "Arrow Rotation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 16.dp)
    ) {
        // Toggle Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp) // Generous touch target
                .clickable { isExpanded = !isExpanded },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Toggle Currency Drawer",
                modifier = Modifier.rotate(arrowRotation),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Currency Pad
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            CurrencyPad(onEvent = onEvent)
        }

        // Number Pad
        NumberPad(
            onEvent = onEvent,
            modifier = Modifier.weight(1f)
        )
    }
}