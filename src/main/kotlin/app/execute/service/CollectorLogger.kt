package app.execute.service

import logger.Logger

class CollectorLogger : Logger {
    private val logs = mutableListOf<String>()

    override fun log(message: String) {
        logs.add(message)
    }

    fun getLogs(): List<String> {
        return logs
    }
}
