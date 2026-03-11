package ph.eece.polycurrency.ui.calculator

import java.util.Stack
import ph.eece.polycurrency.data.local.entity.ExchangeRateEntity

object MathEngine {
    fun evaluate(
        tokens: List<CalculatorToken>,
        currencyDataList: List<ExchangeRateEntity>,
        targetCurrencyCode: String,
        baseCurrencyCode: String
    ): Double {
        // Convert CalculatorTokens to a pure math list (handling implicit mults and scalars)
        val normalizedTokens = normalizeTokens(tokens, currencyDataList, targetCurrencyCode, baseCurrencyCode)

        // Convert Infix to RPN
        val rpnQueue = shuntingYard(normalizedTokens)

        // Solve the RPN
        val resultInBase = solveRPN(rpnQueue)

        val rateMap = currencyDataList.associate { it.currencyCode to it.rateRelativeToBase }
        val targetRate = rateMap[targetCurrencyCode] ?: 1.0
        val baseRate = rateMap[baseCurrencyCode] ?: 1.0

        return resultInBase * (targetRate / baseRate)
    }

    // Normalize Tokens
    // Convert Currencies and Percents into Scalar Coefficients
    private fun normalizeTokens(
        tokens: List<CalculatorToken>,
        currencyDataList: List<ExchangeRateEntity>,
        targetCurrencyCode: String,
        baseCurrencyCode: String
    ): List<MathToken> {
        val result = mutableListOf<MathToken>()
        // Rates lookup map
        val rateMap = currencyDataList.associate { it.currencyCode to it.rateRelativeToBase }
        val baseRate = rateMap[baseCurrencyCode] ?: 1.0

        tokens.forEachIndexed { index, token ->
            val prevToken = tokens.getOrNull(index - 1)

            // Implicit Multiplication
            val isCurrentValue = token is CalculatorToken.Number || token is CalculatorToken.Currency
            val isPrevValue = prevToken is CalculatorToken.Number ||
                    prevToken is CalculatorToken.Currency ||
                    (prevToken is CalculatorToken.Operator && prevToken.symbol == '%') ||
                    (prevToken is CalculatorToken.Parenthesis && prevToken.type == ')')

            // Special Case for parenthesis
            if (token is CalculatorToken.Parenthesis && token.type == '(' && isPrevValue) {
                result.add(MathToken.Op('#')) // 5( -> 5 # (
            }
            // Standard Implicit Multiplication
            else if (isCurrentValue && isPrevValue) {
                result.add(MathToken.Op('#'))
            }


            // Convert Tokens
            when (token) {
                // Currency Residue for Redundancy
                // Currencies should act as constant tokens or digits. USD + EUR
                is CalculatorToken.Currency -> {
                    // Value = (Rate)^-1
                    val tokenRate = rateMap[token.code] ?: 1.0
                    val valueInBase = if (tokenRate != 0.0) baseRate / tokenRate else 0.0
                    result.add(MathToken.Num(valueInBase))
                }

                // Number and Currency Normalization
                // Numbers with no currency shall be treated as currency to the target currency
                // relative to base currency
                // Format is always "USD 100"
                is CalculatorToken.Number -> {
                    val rawValue = token.value.toDoubleOrNull() ?: 0.0
                    val prevToken = tokens.getOrNull(index - 1)

                    if (prevToken is CalculatorToken.Currency) {
                        result.add(MathToken.Num(rawValue))
                    } else {
                        val targetRate = rateMap[targetCurrencyCode] ?: 1.0
                        val valueInBase = rawValue * (baseRate / targetRate)
                        result.add(MathToken.Num(valueInBase))
                    }
                }



                is CalculatorToken.Operator -> {
                    if (token.symbol == '%') {
                        result.add(MathToken.Op('#'))
                        result.add(MathToken.Num(0.01))
                    } else {
                        result.add(MathToken.Op(token.symbol))
                    }
                }

                is CalculatorToken.Parenthesis -> {
                    if (token.type == '(') result.add(MathToken.LeftParen)
                    else result.add(MathToken.RightParen)
                }
            }
        }
        return result
    }

    // Shunting Yard Algorithm
    private fun shuntingYard(tokens: List<MathToken>): List<MathToken> {
        val outputQueue = mutableListOf<MathToken>()
        val operatorStack = Stack<MathToken>()

        val precedence = mapOf(
            'Δ' to 0,
            '+' to 1, '-' to 1,
            '*' to 2, '/' to 2,
            '#' to 3
        )

        for (token in tokens) {
            when (token) {
                is MathToken.Num -> outputQueue.add(token)
                is MathToken.Op -> {
                    while (operatorStack.isNotEmpty() && operatorStack.peek() is MathToken.Op) {
                        val top = operatorStack.peek() as MathToken.Op
                        if ((precedence[top.symbol] ?: 0) >= (precedence[token.symbol] ?: 0)) {
                            outputQueue.add(operatorStack.pop())
                        } else {
                            break
                        }
                    }
                    operatorStack.push(token)
                }
                is MathToken.LeftParen -> operatorStack.push(token)
                is MathToken.RightParen -> {
                    while (operatorStack.isNotEmpty() && operatorStack.peek() !is MathToken.LeftParen) {
                        outputQueue.add(operatorStack.pop())
                    }
                    if (operatorStack.isNotEmpty()) operatorStack.pop() // Pop the '('
                }
            }
        }
        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.pop())
        }
        return outputQueue
    }

    // RPN Solver
    private fun solveRPN(tokens: List<MathToken>): Double {
        val stack = Stack<Double>()

        for (token in tokens) {
            when (token) {
                is MathToken.Num -> stack.push(token.value)
                is MathToken.Op -> {
                    // Binary Operators need 2 numbers
                    if (stack.size < 2) {
                        // TODO Handle unary minus edge case if needed
                        continue
                    }
                    val b = stack.pop()
                    val a = stack.pop()
                    val result = when (token.symbol) {
                        '+' -> a + b
                        '-' -> a - b
                        '*' -> a * b
                        '/' -> if (b != 0.0) a / b else 0.0 // No dev by zero
                        '#' -> a * b
                        'Δ' -> if (a != 0.0) (b - a) / a else 0.0
                        else -> 0.0
                    }
                    stack.push(result)
                }
                else -> {}
            }
        }
        return if (stack.isNotEmpty()) stack.pop() else 0.0
    }

    // Internal class for the parser
    private sealed class MathToken {
        data class Num(val value: Double) : MathToken()
        data class Op(val symbol: Char) : MathToken()
        object LeftParen : MathToken()
        object RightParen : MathToken()
    }
}