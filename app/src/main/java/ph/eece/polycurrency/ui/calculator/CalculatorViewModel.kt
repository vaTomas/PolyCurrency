package ph.eece.polycurrency.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    // private val repository: CurrencyRepository // TODO: Inject later
) : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state = _state.asStateFlow()

    // Button presses
    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.OnDigit -> appendInput(event.digit.toString())
            is CalculatorEvent.OnOperator -> appendInput(" ${event.op} ")
            is CalculatorEvent.OnCurrency -> appendInput(" ${event.code} ")
            CalculatorEvent.OnClear -> _state.update { it.copy(inputExpression = "", liveResult = "") }
            CalculatorEvent.OnDelete -> deleteLastChar()
            CalculatorEvent.OnEvaluate -> calculateResult() // Temporary Placeholder
        }
    }

    private fun appendInput(value: String) {
        _state.update {
            // TODO: Input Validation for Later
            it.copy(inputExpression = it.inputExpression + value)
        }
    }

    private fun deleteLastChar() {
        _state.update {
            val current = it.inputExpression
            if (current.isNotEmpty()) {

                it.copy(inputExpression = current.dropLast(1).trimEnd())
            } else {
                it
            }
        }
    }

    private fun calculateResult() {
        // TODO: CONNECT THE PARSER ENGINE HERE
        _state.update { it.copy(liveResult = "≈ Logic Coming Soon") }
    }
}