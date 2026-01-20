package ph.eece.polycurrency.ui.calculator

// Store to Memory
data class CalculatorState(
    val inputExpression: String = "",       // "50 USD + 10 EUR"
    val liveResult: String = "",            // "≈ 3,400 PHP"
    val history: List<String> = emptyList()
)