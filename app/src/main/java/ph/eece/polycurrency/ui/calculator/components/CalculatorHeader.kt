package ph.eece.polycurrency.ui.calculator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorHeader(
    isHistoryOpen: Boolean,
    onToggleHistory: () -> Unit,
    onMoreOptions: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
//        IconButton(onClick = onToggleHistory) {
//            Icon(
//                imageVector = Icons.Default.Refresh,
//                contentDescription = "History",
//                tint = if (isHistoryOpen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
//            )
//        }
        IconButton(onClick = onMoreOptions) {
            Icon(Icons.Default.MoreVert, contentDescription = "More")
        }
    }
}