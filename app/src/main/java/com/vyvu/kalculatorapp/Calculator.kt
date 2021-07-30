package com.vyvu.kalculatorapp

class Calculator {
    var result = 0.0
    var infix = mutableListOf<String>()
    private var postfix = mutableListOf<String>()

    fun calc(): Double {
        initPostfix()
        val stack = Stack()
        for (i in postfix) {
            val num = i.toDoubleOrNull()
            if (num != null) {
                stack.push(num)
            } else {
                val num1 = stack.pop().toDouble()
                val num2 = stack.pop().toDouble()
                when (i) {
                    OPERATOR_ADD -> stack.push(num2 + num1)
                    OPERATOR_SUBTRACT -> stack.push(num2 - num1)
                    OPERATOR_DIVISION -> stack.push(num2 / num1)
                    OPERATOR_MULTIPLY -> stack.push(num2 * num1)
                }
            }
        }
        result = if (!stack.isEmpty()) stack.pop().toDouble() else 0.0
        return result
    }

    private fun initPostfix() {
        infix.removeAll(listOf(EMPTY_STRING))
        postfix = mutableListOf()
        val stack = Stack()
        for (i in infix) {
            val num = i.toDoubleOrNull()
            when {
                num != null -> postfix += "$num"
                i == BRACKET_OPEN -> stack.push(i)
                i == BRACKET_CLOSE -> {
                    while (!stack.isEmpty() && stack.top() != BRACKET_OPEN) postfix += stack.pop()
                    stack.pop()
                }
                else -> {
                    while (!stack.isEmpty() && priority(i) <= priority(stack.top())) {
                        postfix += stack.pop()
                    }
                    stack.push(i)
                }
            }
        }
        while (!stack.isEmpty()) {
            if (stack.top() == BRACKET_OPEN) {
                postfix = mutableListOf()
                break
            }
            postfix += stack.pop()
        }
    }

    private fun priority(operator: String): Int = when (operator) {
        OPERATOR_ADD, OPERATOR_SUBTRACT -> 1
        OPERATOR_MULTIPLY, OPERATOR_DIVISION -> 2
        else -> -1
    }

    private class Stack {
        private var arr = mutableListOf<String>()
        fun push(a: Any) {
            arr += a.toString()
        }

        fun pop(): String {
            if (isEmpty()) return EMPTY_STRING
            val res = arr[arr.size - 1]
            arr = ArrayList(arr.dropLast(1))
            return res
        }

        fun top() = if (isEmpty()) EMPTY_STRING else arr[arr.size - 1]
        fun isEmpty() = arr.size == 0
    }

    fun del() {
        infix.removeLastOrNull()
        infix.removeLastOrNull()
    }

    fun refresh() {
        infix = mutableListOf()
        postfix = mutableListOf()
    }

    override fun toString(): String {
        return infix.joinToString(EMPTY_STRING)
    }

    companion object {
        const val OPERATOR_ADD = "+"
        const val OPERATOR_SUBTRACT = "-"
        const val OPERATOR_MULTIPLY = "*"
        const val OPERATOR_DIVISION = "/"
        const val BRACKET_OPEN = "("
        const val BRACKET_CLOSE = ")"
        const val EMPTY_STRING = ""
    }
}
