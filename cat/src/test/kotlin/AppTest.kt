/*
 * Copyright (c) Artnet Gmsolution. All rights reserved.
 *
 * テストクラス
 */
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import vn.gmgroup.cat.Cat
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue

class AppTest {
    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(standardOut)
        outputStreamCaptor.reset()
    }

    @Test
    fun `testShowFileWithNumber`() {
        val app = Cat()
        app.main(arrayOf("-n", "data/data.txt"))
        assertTrue(outputStreamCaptor.toString().trim().contains("2: TXT test file"),
            "Failed show file with number")
    }

    @Test
    fun `testShowFileWithNumberForNonemptyLine`() {
        val app = Cat()
        app.main(arrayOf("-b", "data/data.txt"))
        assertTrue(outputStreamCaptor.toString().trim().contains("1: TXT test file"),
            "Failed show file with number for nom empty line")
    }

    @Test
    fun `testShowFileWithEnd`() {
        val app = Cat()
        app.main(arrayOf("-E", "data/data.txt"))
        assertTrue(outputStreamCaptor.toString().trim().contains("TXT test file$"),
            "Failed show file with end for line")
    }

    @Test
    fun `testShowFileSqueezeBlank`() {
        val app = Cat()
        app.main(arrayOf("-s", "data/files.txt"))
        assertTrue(outputStreamCaptor.toString().trim().contains("TXT test file" +
                System.lineSeparator() + System.lineSeparator() +
                "Purpose: Provide example of this file type"),
            "Failed show file with suppressing repeated empty output lines")
    }

    @Test
    fun `testShowFileWithTab`() {
        val app = Cat()
        app.main(arrayOf("-T", "data/files.txt"))
        assertTrue(outputStreamCaptor.toString().trim().contains("Document file ^I^Itype: TXT"),
            "Failed show file with tab")
    }

    @Test
    fun `testShowFileWithNonPrintingCharacter`() {
        val app = Cat()
        app.main(arrayOf("-v", "data/nonprinting.txt"))
        assertTrue(outputStreamCaptor.toString().trim().contains("aa^K^K"),
            "Failed show file with non printing character")
    }

    @Test
    fun `testShowFileWithGlobPatternPath`() {
        val app = Cat()
        app.main(arrayOf("-v", "data/*.txt"))
        assertTrue(outputStreamCaptor.toString().trim().contains("File 1")
                && outputStreamCaptor.toString().trim().contains("File 2")
                && outputStreamCaptor.toString().trim().contains("File 3"),
            "Failed show file with glob pattern path")
    }
}
