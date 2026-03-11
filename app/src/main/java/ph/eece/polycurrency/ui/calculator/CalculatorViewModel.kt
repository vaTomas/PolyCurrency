package ph.eece.polycurrency.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import ph.eece.polycurrency.data.CurrencyRepository

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state = _state.asStateFlow()

    init {
        // 1. Listen to the Local Database continuously
        viewModelScope.launch {
            repository.allRates.collect { databaseRates ->
                // Every time DB change, update  state
                _state.update { it.copy(currencyRates = databaseRates) }

                // Recalculate  result immediately so the UI updates if rates changed
                calculateResult()
            }
        }

        // 2. Background Poll internet for fresh rates
        viewModelScope.launch {
            repository.syncRates(backendBase = "PHP")
        }
    }

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.OnDigit -> {
                if (event.digit == '.') addDecimal() else addNumber(event.digit)
                calculateResult()
            }
            is CalculatorEvent.OnOperator -> { addOperator(event.op); calculateResult() }

            is CalculatorEvent.OnClear -> { onClear() } // Reset
            is CalculatorEvent.OnDelete -> { onDelete(); calculateResult() }
            is CalculatorEvent.OnCurrency -> { addCurrency(event.code); calculateResult() }
            is CalculatorEvent.OnEvaluate -> { calculateResult() }
            is CalculatorEvent.OnPercent -> { addPercent(); calculateResult() }
            is CalculatorEvent.OnSmartParenthesis -> { addSmartParenthesis(); calculateResult() }
            // TODO Change calculateResult() realtime implementation to reactive consequence
            is CalculatorEvent.OnToggleHistory -> {
                _state.update { it.copy(isHistoryOpen = !it.isHistoryOpen) }
            }
            is CalculatorEvent.OnToggleExtras -> {
                _state.update { it.copy(isExtrasOpen = !it.isExtrasOpen) }
            }
            is CalculatorEvent.OnChangeTargetCurrency -> {
                _state.update { it.copy(targetCurrencyCode = event.code) }
                calculateResult()
            }
            is CalculatorEvent.OnToggleActiveCurrency -> {
                _state.update { currentState ->
                    val currentList = currentState.activeCurrencies.toMutableList()
                    if (currentList.contains(event.code)) {
                        currentList.remove(event.code) // Uncheck
                    } else {
                        currentList.add(event.code)    // Check
                    }
                    currentState.copy(activeCurrencies = currentList)
                }
            }
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

    private fun addSmartParenthesis() {
        _state.update { currentState ->
            val tokens = currentState.tokens.toMutableList()

            // Count Parenthesis
            val openCount = tokens.count { it is CalculatorToken.Parenthesis && it.type == '(' }
            val closeCount = tokens.count { it is CalculatorToken.Parenthesis && it.type == ')' }
            val parenthesisBalance = openCount - closeCount

            val lastToken = tokens.lastOrNull()

            val shouldOpen = when {
                parenthesisBalance == 0 -> true

                else -> when (lastToken) {
                    is CalculatorToken.Operator -> true
                    is CalculatorToken.Parenthesis -> lastToken.type == '('
                    else -> false
                }
            }

            if (shouldOpen) {
                tokens.add(CalculatorToken.Parenthesis('('))
            } else {
                tokens.add(CalculatorToken.Parenthesis(')'))
            }

            currentState.copy(tokens = tokens)
        }
    }

    private fun addCurrency(code: String) {
        _state.update { currentState ->
            val tokens = currentState.tokens.toMutableList()
            val lastToken = tokens.lastOrNull()

            // If Empty -> Start with Currency
            if (lastToken == null) {
                tokens.add(CalculatorToken.Currency(code))
            }
            // If Latest is Operator -> Just Append
            else if (lastToken is CalculatorToken.Operator || lastToken is CalculatorToken.Parenthesis) {
                tokens.add(CalculatorToken.Currency(code))
            }
            // If Latest is Currency -> Replace
            else if (lastToken is CalculatorToken.Currency) {
                tokens[tokens.lastIndex] = CalculatorToken.Currency(code)
            }
            // If Latest is Number
            else if (lastToken is CalculatorToken.Number) {
                // Check the token BEFORE the number
                val secondLastIndex = tokens.lastIndex - 1
                val secondLastToken = tokens.getOrNull(secondLastIndex)

                if (secondLastToken is CalculatorToken.Currency) {
                    // If has existing currency -> Replace
                    tokens[secondLastIndex] = CalculatorToken.Currency(code)
                } else {
                    // No currency -> Insert BEFORE the number
                    tokens.add(tokens.lastIndex, CalculatorToken.Currency(code))
                }
            }
            currentState.copy(tokens = tokens)
        }
    }

    private fun onDelete() {
        _state.update { currentState ->
            val tokens = currentState.tokens.toMutableList()
            val lastToken = tokens.lastOrNull()

            if (lastToken != null) {
                // If Number
                if (lastToken is CalculatorToken.Number) {
                    // Drop last char.
                    val newValue = lastToken.value.dropLast(1)

                    if (newValue.isEmpty()) {
                        // If resulting number is empty, delee token
                        tokens.removeAt(tokens.lastIndex)
                    } else {
                        // If number remains, update
                        tokens[tokens.lastIndex] = lastToken.copy(value = newValue)
                    }
                } else {
                    // If not Number, delete token
                    tokens.removeAt(tokens.lastIndex)
                }
            }

            currentState.copy(tokens = tokens)
        }
    }

    private fun onClear() {
        _state.update { currentState ->
            currentState.copy(
                tokens = emptyList(),
                liveResult = ""
            )
        }
    }

    private fun calculateResult() {
        _state.update { currentState ->
            if (currentState.tokens.isEmpty()) return@update currentState.copy(liveResult = "")

            // Use the rates from the database.
            val currentRates = currentState.currencyRates

            // If the database is empty (first launch, no internet), just return 0.0 or wait.
            if (currentRates.isEmpty()) return@update currentState.copy(liveResult = "Updating...")

            val rawResultInPHP = try {
                MathEngine.evaluate(currentState.tokens, currentRates) // Pass the real rates
            } catch (e: Exception) { 0.0 }

            // Find the target rate
            val targetRate = currentRates.find { it.currencyCode == currentState.targetCurrencyCode }?.rateRelativeToBase ?: 1.0
            val convertedResult = if (targetRate != 0.0) rawResultInPHP / targetRate else 0.0

            val resultString = "${currentState.targetCurrencyCode} " + String.format("%,.2f", convertedResult)

            currentState.copy(liveResult = resultString)
        }
    }

    fun toggleCurrency(code: String) {
        _state.update { currentState ->
            val currentList = currentState.activeCurrencies.toMutableList()

            if (currentList.contains(code)) {
                // Prevent removing the last currency
                if (currentList.size > 1) currentList.remove(code)
            } else {
                currentList.add(code)
            }

            currentState.copy(activeCurrencies = currentList)
        }
    }
}

private fun CalculatorViewModel.getSymbolFor(targetCurrencyCode: String) {}
