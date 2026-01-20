package ph.eece.polycurrency.ui.calculator

sealed class CalculatorEvent {
    data class OnDigit(val digit: Char): CalculatorEvent()
    data class OnOperator(val op: Char): CalculatorEvent()
    data class OnCurrency(val code: String): CalculatorEvent()
    object OnClear : CalculatorEvent()
    object OnDelete : CalculatorEvent()
    object OnEvaluate : CalculatorEvent()
}