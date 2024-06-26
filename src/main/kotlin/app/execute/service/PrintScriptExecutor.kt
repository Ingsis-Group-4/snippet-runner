package app.execute.service

import ast.AST
import formatter.ProgramNodeFormatter
import formatter.factory.FormatterMapFactory
import formatter.rule.FormattingRule
import interpreter.StatementInterpreter
import interpreter.factory.getInterpreterMap
import lexer.Lexer
import lexer.getTokenMap
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import parser.factory.ProgramParserFactoryV2
import parser.factory.StatementParserFactory
import parser.result.FailureResult
import parser.result.ParserResult
import parser.result.SuccessResult
import reader.StatementFileReader
import runner.Runner
import sca.Report
import sca.StaticCodeAnalyzer
import sca.factory.DefaultSCARuleFactory
import version.Version
import java.io.InputStream

@Service
class PrintScriptExecutor {
    private val logger = LoggerFactory.getLogger(PrintScriptExecutor::class.java)

    fun interpret(
        snippet: InputStream,
        inputs: List<String> = listOf(),
    ): ExecuteOutput {
        logger.info("Received request to interpret snippet")
        val printScriptRunner = Runner()
        val errorLogger = CollectorLogger()
        val outputLogger = CollectorLogger()
        val inputProvider = ListInputProvider(inputs)

        logger.info("Running snippet")
        printScriptRunner.run(
            reader = StatementFileReader(snippet, Version.V2),
            parser = StatementParserFactory.create(Version.V2),
            interpreter = StatementInterpreter(getInterpreterMap(Version.V2)),
            errorLogger = errorLogger,
            readInputFunction = inputProvider,
            logger = outputLogger,
        )
        logger.info("Snippet execution complete, returning output")
        return ExecuteOutput(
            outputs = outputLogger.getLogs(),
            errors = errorLogger.getLogs(),
        )
    }

    fun format(
        snippet: String,
        config: String,
    ): String {
        logger.info("Received request to format snippet")
        val formatter = ProgramNodeFormatter()

        try {
            logger.info("Formatting snippet")
            val formattedSnippet =
                formatter.format(getAST(snippet), FormattingRule(config), FormatterMapFactory().createFormatterMap())
            logger.info("Snippet formatted successfully")
            return formattedSnippet
        } catch (e: Exception) {
            logger.error("Error: Invalid snippet could not be formatted. Snippet: $snippet", e)
            return "$snippet\n\n Error: Invalid snippet could not be formatted"
        }
    }

    fun lint(
        snippet: String,
        config: String,
    ): LintOutput {
        logger.info("Received request to lint snippet")
        val linter = StaticCodeAnalyzer(DefaultSCARuleFactory().getRules(config))

        logger.info("Linting snippet")
        val analysis = linter.analyze(getAST(snippet))
        logger.info("Snippet linted successfully")
        return toLintOutput(analysis)
    }

    private fun getAST(snippet: String): AST {
        val lexer = Lexer(getTokenMap(Version.V2))
        val parser = ProgramParserFactoryV2.create(Version.V2)

        logger.info("Parsing snippet")
        val parseResult = parser.parse(lexer.lex(snippet))

        return getSuccessASTOrThrow(parseResult)
    }

    private fun getSuccessASTOrThrow(result: ParserResult): AST {
        return when (result) {
            is FailureResult -> {
                logger.error("Error: Snippet could not be parsed. Reason: ${result.message}")
                throw SnippetParsingException(result.message)
            }

            is SuccessResult -> {
                logger.info("Got AST successfully from snippet parsing")
                result.value
            }
        }
    }

    private fun toLintOutput(report: Report): LintOutput {
        logger.info("Received report, converting to LintOutput")
        val isSuccess = report.ruleFailures.isEmpty()
        val failures = report.ruleFailures.stream().map { "${it.message} at ${it.position}" }.toList()

        return LintOutput(isSuccess, failures)
    }
}

class SnippetParsingException(reason: String) : Exception(reason)
