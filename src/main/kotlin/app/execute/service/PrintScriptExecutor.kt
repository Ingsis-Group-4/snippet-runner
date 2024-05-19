package app.execute.service

import ast.AST
import formatter.ProgramNodeFormatter
import formatter.factory.FormatterMapFactory
import formatter.rule.FormattingRule
import interpreter.StatementInterpreter
import interpreter.factory.getInterpreterMap
import lexer.Lexer
import lexer.getTokenMap
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
    fun interpret(
        snippet: InputStream,
        inputs: List<String> = listOf(),
    ): ExecuteOutput {
        val printScriptRunner = Runner()
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

    fun format(
        snippet: String,
        config: String,
    ): String {
        val formatter = ProgramNodeFormatter()

        val formattedSnippet =
            formatter.format(getAST(snippet), FormattingRule(config), FormatterMapFactory().createFormatterMap())
        return formattedSnippet
    }

    fun lint(
        snippet: String,
        config: String,
    ): LintOutput {
        val linter = StaticCodeAnalyzer(DefaultSCARuleFactory().getRules(config))

        val analysis = linter.analyze(getAST(snippet))

        return toLintOutput(analysis)
    }

    private fun getAST(snippet: String): AST {
        val lexer = Lexer(getTokenMap(Version.V2))
        val parser = ProgramParserFactoryV2.create(Version.V2)

        val parseResult = parser.parse(lexer.lex(snippet))

        return getSuccessASTOrThrow(parseResult)
    }

    private fun getSuccessASTOrThrow(result: ParserResult): AST {
        return when (result) {
            is FailureResult -> throw SnippetParsingException(result.message)
            is SuccessResult -> result.value
        }
    }

    private fun toLintOutput(report: Report): LintOutput {
        val isSuccess = report.ruleFailures.isEmpty()
        val failures = report.ruleFailures.stream().map { "${it.message} at ${it.position}" }.toList()

        return LintOutput(isSuccess, failures)
    }
}

class SnippetParsingException(reason: String) : Exception(reason)
