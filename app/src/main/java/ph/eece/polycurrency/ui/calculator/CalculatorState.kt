package ph.eece.polycurrency.ui.calculator

import ph.eece.polycurrency.data.UserPreferencesRepository
import ph.eece.polycurrency.data.local.entity.ExchangeRateEntity

// Store to Memory
data class CalculatorState(
    val tokens: List<CalculatorToken> = emptyList(),
    val liveResult: String = "",
    val activeCurrencies: List<String> = listOf("USD", "PHP", "EUR", "VND"), // TODO make dynamic

    // UI Toggles
    val isHistoryOpen: Boolean = false,
    val isExtrasOpen: Boolean = false,

    val targetCurrencyCode: String = UserPreferencesRepository.DEFAULT_TARGET,
    val baseCurrencyCode: String = UserPreferencesRepository.DEFAULT_BASE,
    val currencyRates: List<ExchangeRateEntity> = emptyList(),

    val history: List<String> = emptyList()
) {
    // Convert Token List to formatted String
    // Projection Layer
    val inputExpression: String
        get() = tokens.joinToString(" ") { token ->
            when (token) {
                is CalculatorToken.Number -> formatWithCommas(token.value)
                is CalculatorToken.Operator -> when (token.symbol) {
                    '/' -> "÷"
                    '*' -> "×"
                    'Δ' -> "Δ%"
                    else -> token.symbol.toString()
                }
                is CalculatorToken.Currency -> token.code
                is CalculatorToken.Parenthesis -> token.type.toString()
            }
        }

    private fun formatWithCommas(raw: String): String {
        if (raw.isEmpty()) return ""

        val parts = raw.split(".")
        val integerPart = parts[0]

        // Number Part
        // Comma Every 3 digits
        val formattedInt = integerPart.replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), ",")

        // Decimal Part
        return if (parts.size > 1) {
            "$formattedInt.${parts[1]}"
        } else {
            // Show ending point
            if (raw.endsWith(".")) "$formattedInt." else formattedInt
        }
    }
}