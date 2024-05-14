package app.execute.service

import interpreter.readInputFunction.ReadInputFunction

class ListInputProvider(
    private val inputs: List<String>,
) : ReadInputFunction {
    private var current = 0

    override fun read(string: String): String? {
        val next = inputs[current]
        current++
        return next
    }
}
