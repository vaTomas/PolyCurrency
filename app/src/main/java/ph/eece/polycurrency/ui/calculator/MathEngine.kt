package ph.eece.polycurrency.ui.calculator

import ph.eece.polycurrency.ui.currency.CurrencyData
import java.util.Stack

object MathEngine {
    fun evaluate(
        tokens: List<CalculatorToken>,
        currencyDataList: List<CurrencyData>
    ): Double {
        // Convert CalculatorTokens to a pure math list (handling implicit mults and scalars)
        val normalizedTokens = normalizeTokens(tokens, currencyDataList)

        // Convert Infix to RPN
        val rpnQueue = shuntingYard(normalizedTokens)

        // Solve the RPN
        return solveRPN(rpnQueue)
    }

    // Normalize Tokens
    // Convert Currencies and Percents into Scalar Coefficients
    private fun normalizeTokens(
        tokens: List<CalculatorToken>,
        currencyDataList: List<CurrencyData>
    ): List<MathToken> {
        val result = mutableListOf<MathToken>()
        // Rates lookup map
        val rateMap = currencyDataList.associate { it.code to it.rateToPHP }

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
                result.add(MathToken.Op('*'))
            }
            // Standard Implicit Multiplication
            else if (isCurrentValue && isPrevValue) {
                result.add(MathToken.Op('*'))
            }


            // Convert Tokens
            when (token) {
                is CalculatorToken.Number -> {
                    result.add(MathToken.Num(token.value.toDoubleOrNull() ?: 0.0))
                }
                is CalculatorToken.Currency -> {
                    // Value = (Rate)^-1
                    val rate = rateMap[token.code] ?: 1.0
                    val scalarValue = if (rate != 0.0) 1.0 / rate else 0.0
                    result.add(MathToken.Num(scalarValue))
                }
                is CalculatorToken.Operator -> {
                    if (token.symbol == '%') {
                        // Treat percent as "* 0.01"
                        result.add(MathToken.Op('*'))
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
            '+' to 1, '-' to 1,
            '*' to 2, '/' to 2
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