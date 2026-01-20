package ph.eece.polycurrency.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = CircleShape,
    aspectRatio: Float = 1f,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(shape)
                .background(backgroundColor)
                .clickable(onClick = onClick)
                .aspectRatio(aspectRatio, matchHeightConstraintsFirst = false)
                .fillMaxSize()
        ) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = textColor
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CalculatorButtonPreview() {
    MaterialTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            // Standard Button
            CalculatorButton(
                symbol = "5",
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            // Wide Button (The "0" case)
            CalculatorButton(
                symbol = "0",
                onClick = {},
                modifier = Modifier.weight(2f), // Takes 2 grid cells
                aspectRatio = 2f, // Renders as Pill
                shape = RoundedCornerShape(50) // Pill shape
            )
        }
    }
}