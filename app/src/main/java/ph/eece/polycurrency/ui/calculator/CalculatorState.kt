package ph.eece.polycurrency.ui.calculator

// Store to Memory
data class CalculatorState(
    val tokens: List<CalculatorToken> = emptyList(),
    val liveResult: String = "",

    val history: List<String> = emptyList()
) {
    // Convert Token List to formatted String
    val inputExpression: String
        get() = tokens.joinToString(" ") { token ->
            when (token) {
                is CalculatorToken.Number -> token.value
                is CalculatorToken.Operator -> token.symbol.toString()
                is CalculatorToken.Currency -> token.code
            }
        }
}