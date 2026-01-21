package ph.eece.polycurrency.ui.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state = _state.asStateFlow()

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.OnDigit -> {
                if (event.digit == '.') addDecimal() else addNumber(event.digit)
            }
            is CalculatorEvent.OnOperator -> addOperator(event.op)

            is CalculatorEvent.OnClear -> _state.update { CalculatorState() } // Reset
            is CalculatorEvent.OnDelete -> { /* TODO */ }
            is CalculatorEvent.OnCurrency -> { /* TODO */ }
            is CalculatorEvent.OnEvaluate -> { /* TODO */ }
            is CalculatorEvent.OnPercent -> addPercent()
        }
    }

    // Numbers
    // If latest is number -> append. Else -> create new number.
    private fun addNumber(digit: Char) {
        _state.update { currentState ->
            val tokens = currentState.tokens.toMutableList()
            val lastToken = tokens.lastOrNull()

            if (lastToken is CalculatorToken.Number) {
                // Append digit to existing Number token
                val newNumber = lastToken.copy(value = lastToken.value + digit)
                tokens[tokens.lastIndex] = newNumber
            } else {
                // Create new Number token
                tokens.add(CalculatorToken.Number(digit.toString()))
            }
            currentState.copy(tokens = tokens)
        }
    }

    // Period Decimal Point
    // Treat as number. If latest is number -> append (if valid). Else -> create new '0.' token.
    private fun addDecimal() {
        _state.update { currentState ->
            val tokens = currentState.tokens.toMutableList()
            val lastToken = tokens.lastOrNull()

            if (lastToken is CalculatorToken.Number) {
                // A number must only have 1 period.
                if (!lastToken.value.contains(".")) {
                    val newNumber = lastToken.copy(value = lastToken.value + ".")
                    tokens[tokens.lastIndex] = newNumber
                }
                // If it already has a period, do nothing.
            } else {
                // Create new number token then add period.
                // We make it "0." because "." is mathematically unsafe.
                tokens.add(CalculatorToken.Number("0."))
            }
            currentState.copy(tokens = tokens)
        }
    }

    // -Operators
    // If previous is operator -> replace. Else -> create new."
    private fun addOperator(newOp: Char) {
        _state.update { currentState ->
            val tokens = currentState.tokens.toMutableList()
            val lastToken = tokens.lastOrNull()
            val secondLastToken = tokens.getOrNull(tokens.lastIndex - 1)

            // Double Operator
            val isDoubleOp = lastToken is CalculatorToken.Operator && lastToken.symbol == '-' &&
                    secondLastToken is CalculatorToken.Operator &&
                    (secondLastToken.symbol == '/' || secondLastToken.symbol == '*')

            if (isDoubleOp) {
                if (newOp == '-') {
                    // Do Nothing
                } else {
                    // Strip the minus. Ignore the new input. Revert to Base.
                    tokens.removeAt(tokens.lastIndex)
                }
            }

            else if (lastToken is CalculatorToken.Operator) {
                val currentOp = lastToken.symbol

                if ((currentOp == '/' || currentOp == '*') && newOp == '-') {
                    tokens.add(CalculatorToken.Operator(newOp))
                }
                else {
                    tokens[tokens.lastIndex] = CalculatorToken.Operator(newOp)
                }
            }
            // Detect Empty State
            else if (tokens.isEmpty()) {
                // Allow negative start
                if (newOp == '-') {
                    tokens.add(CalculatorToken.Operator(newOp))
                }
                // Ignore others
            }
            // Standard Number State
            else {
                tokens.add(CalculatorToken.Operator(newOp))
            }

            currentState.copy(tokens = tokens)
        }
    }

    private fun addPercent() {
        _state.update { currentState ->
            val tokens = currentState.tokens.toMutableList()
            val lastToken = tokens.lastOrNull()

            val isValidPredecessor = when (lastToken) {
                is CalculatorToken.Number -> true // X -> X%
                is CalculatorToken.Operator -> lastToken.symbol == '%' // % -> %% (Stacking)
                else -> false // Empty or Binary Operator -> Ignore
            }

            if (isValidPredecessor) {
                tokens.add(CalculatorToken.Operator('%'))
            }

            currentState.copy(tokens = tokens)
        }
    }
}
