package app.execute.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class PrintscriptExecutorTest {
    private val executor = PrintScriptExecutor()

    private val inputBase = "src/test/resources/app/execute/input"
    private val expectedBase = "src/test/resources/app/execute/expected"
    private val formatConfigFilePath = "src/test/resources/app/execute/config/format.config.json"
    private val lintConfigFilePath = "src/test/resources/app/execute/config/lint.config.json"

    @Test
    fun `001 Interpret with empty InputStream`() {
        val result = executor.interpret(InputStream.nullInputStream())

        Assertions.assertEquals(0, result.errors.size)
        Assertions.assertEquals(0, result.outputs.size)
    }

    @Test
    fun `002 Interpret file with single println`() {
        val inputFile = File("$inputBase/002.ps")
        val input = FileInputStream(inputFile)

        val result = executor.interpret(input)

        Assertions.assertEquals(0, result.errors.size)
        Assertions.assertEquals(1, result.outputs.size)
        Assertions.assertEquals("Hello", result.outputs[0])
    }

    @Test
    fun `003 Interpret file with error`() {
        val inputFile = File("$inputBase/003.ps")
        val input = FileInputStream(inputFile)

        val result = executor.interpret(input)

        Assertions.assertEquals(1, result.errors.size)
        Assertions.assertEquals(0, result.outputs.size)
    }

    @Test
    fun `004 Interpret file with input`() {
        val inputFile = File("$inputBase/004.ps")
        val input = FileInputStream(inputFile)

        val result = executor.interpret(input, listOf("Input"))

        Assertions.assertEquals(0, result.errors.size)
        Assertions.assertEquals(2, result.outputs.size)
        Assertions.assertEquals("Message", result.outputs[0])
        Assertions.assertEquals("Input", result.outputs[1])
    }

    @Test
    fun `005 Interpret file with multiple inputs`() {
        val inputFile = File("$inputBase/005.ps")
        val input = FileInputStream(inputFile)

        val result = executor.interpret(input, listOf("Input1", "Input2"))

        Assertions.assertEquals(0, result.errors.size)
        Assertions.assertEquals(4, result.outputs.size)
        Assertions.assertEquals("Message1", result.outputs[0])
        Assertions.assertEquals("Input1", result.outputs[1])
        Assertions.assertEquals("Message2", result.outputs[2])
        Assertions.assertEquals("Input2", result.outputs[3])
    }

    @Test
    fun `006 Format file with one line`() {
        val config = File(formatConfigFilePath).readText()
        val input = File("$inputBase/006.ps").readText()
        val expected = File("$expectedBase/006.ps").readText()

        val result = executor.format(input, config)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `007 Format file with invalid snippet should return error message`() {
        val config = File(formatConfigFilePath).readText()
        val input = File("$inputBase/007.ps").readText()

        val result = executor.format(input, config)
        Assertions.assertTrue(result.contains("Error: Invalid snippet could not be formatted"))
    }

    @Test
    fun `008 lint file with valid snippet and one rule failure should return report`() {
        val config = File(lintConfigFilePath).readText()
        val input = File("$inputBase/008.ps").readText()

        val result = executor.lint(input, config)

        Assertions.assertFalse(result.isSuccess)
        Assertions.assertEquals(1, result.failures.size)
        Assertions.assertEquals("Variable 'a_var' does not follow naming rule at (line: 1, column: 5)", result.failures[0])
    }
}
