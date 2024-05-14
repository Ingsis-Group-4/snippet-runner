package app.execute.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class PrintscriptExecutorTest {
    private val executor = PrintScriptExecutor()

    @Test
    fun `001 Execute with empty InputStream`() {
        val result = executor.execute(InputStream.nullInputStream())

        Assertions.assertEquals(0, result.errors.size)
        Assertions.assertEquals(0, result.outputs.size)
    }

    @Test
    fun `002 Execute file with single println`() {
        val inputFile = File("src/test/resources/app/execute/002.ps")
        val input = FileInputStream(inputFile)

        val result = executor.execute(input)

        Assertions.assertEquals(0, result.errors.size)
        Assertions.assertEquals(1, result.outputs.size)
        Assertions.assertEquals("Hello", result.outputs[0])
    }

    @Test
    fun `003 Execute file with error`() {
        val inputFile = File("src/test/resources/app/execute/003.ps")
        val input = FileInputStream(inputFile)

        val result = executor.execute(input)

        Assertions.assertEquals(1, result.errors.size)
        Assertions.assertEquals(0, result.outputs.size)
    }

    @Test
    fun `004 Execute file with input`() {
        val inputFile = File("src/test/resources/app/execute/004.ps")
        val input = FileInputStream(inputFile)

        val result = executor.execute(input, listOf("Input"))

        Assertions.assertEquals(0, result.errors.size)
        Assertions.assertEquals(2, result.outputs.size)
        Assertions.assertEquals("Message", result.outputs[0])
        Assertions.assertEquals("Input", result.outputs[1])
    }

    @Test
    fun `005 Execute file with multiple inputs`() {
        val inputFile = File("src/test/resources/app/execute/005.ps")
        val input = FileInputStream(inputFile)

        val result = executor.execute(input, listOf("Input1", "Input2"))

        Assertions.assertEquals(0, result.errors.size)
        Assertions.assertEquals(4, result.outputs.size)
        Assertions.assertEquals("Message1", result.outputs[0])
        Assertions.assertEquals("Input1", result.outputs[1])
        Assertions.assertEquals("Message2", result.outputs[2])
        Assertions.assertEquals("Input2", result.outputs[3])
    }
}
