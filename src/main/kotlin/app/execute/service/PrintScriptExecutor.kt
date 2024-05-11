package app.execute.service

import interpreter.StatementInterpreter
import interpreter.factory.getInterpreterMap
import parser.factory.StatementParserFactory
import reader.StatementFileReader
import runner.Runner
import version.Version
import java.io.InputStream

class PrintScriptExecutor {
    private val printScriptRunner = Runner()

    fun execute(
        snippet: InputStream,
        inputs: List<String> = listOf(),
    ): ExecuteOutput {
        val errorLogger = CollectorLogger()
        val outputLogger = CollectorLogger()
        val inputProvider = ListInputProvider(inputs)

        printScriptRunner.run(
            reader = StatementFileReader(snippet, Version.V2),
            parser = StatementParserFactory.create(Version.V2),
            interpreter = StatementInterpreter(getInterpreterMap(Version.V2)),
            errorLogger = errorLogger,
            readInputFunction = inputProvider,
            logger = outputLogger,
        )

        return ExecuteOutput(
            outputs = outputLogger.getLogs(),
            errors = errorLogger.getLogs(),
        )
    }
}
