package ph.eece.polycurrency.ui.calculator

sealed class CalculatorToken {
    data class Number(val value: String) : CalculatorToken()
    data class Operator(val symbol: Char) : CalculatorToken()
    data class Currency(val code: String) : CalculatorToken()
    data class Parenthesis(val type: Char) : CalculatorToken()
}