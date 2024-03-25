import java.io.File
import java.lang.Exception

class OptionsParser {
    var numberNonBlank: Boolean = false
    var showEnds: Boolean = false
    var number: Boolean = false
    var squeezeBlank: Boolean = false
    var showTabs: Boolean = false
    var showNonPrinting: Boolean = false
    var help: Boolean = false
    var version: Boolean = false
    val filePaths: MutableList<String> = mutableListOf()
    var errorMessage: String? = null

    private fun setOption(option: Char) {
        when (option) {
            'A' -> {
                showNonPrinting = true
                showEnds = true
                showTabs = true
            }
            'b' -> numberNonBlank = true
            'e' -> {
                showNonPrinting = true
                showEnds = true
            }
            'E' -> showEnds = true
            'n' -> number = true
            's' -> squeezeBlank = true
            't' -> {
                showNonPrinting = true
                showTabs = true
            }
            'T' -> showTabs = true
            'u' -> {} // 無視
            'v' -> showNonPrinting = true
            else -> {
                throw Exception("Unknown option: -$option")
            }
        }
    }

    fun parseOptions(options: Array<String>) {
        try {
            options.forEach { option ->
                if (!option.startsWith("-")) {
                    filePaths.add(option)
                } else {
                    when {
                        option.startsWith("--") -> {
                            when (option) {
                                "--show-all" -> {
                                    showNonPrinting = true
                                    showEnds = true
                                    showTabs = true
                                }
                                "--number-nonblank" -> numberNonBlank = true
                                "--show-ends" -> showEnds = true
                                "--number" -> number = true
                                "--squeeze-blank" -> squeezeBlank = true
                                "--show-tabs" -> showTabs = true
                                "--show-nonprinting" -> showNonPrinting = true
                                "--help" -> help = true
                                "--version" -> version = true
                                else -> {
                                    throw Exception("Unknown option: -$option")
                                }
                            }
                        }
                        option.startsWith("-") -> {
                            option.drop(1).forEach { charOption ->
                                setOption(charOption)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Caught an exception: ${e.message}")
        }

        // -bは-nを上書きする
        if (numberNonBlank) {
            number = false
        }
    }
}

class Cat() {
    fun main(args: Array<String>) {
        val parser = OptionsParser()
        parser.parseOptions(args)

        if(parser.errorMessage != null) {
            println(parser.errorMessage)
        }

        val filePaths = parser.filePaths

        for (filePath in filePaths) {
            val file = File(filePath)
            if (file.exists()) {
                printFileContent(file, parser)
            } else {
                println("$filePath: No such file or directory")
            }
        }
    }

    fun printFileContent(file: File, parser: OptionsParser) {
        file.useLines { lines ->
            var lineNumber = 1
            var lastLineWasBlank = false

            for (line in lines) {
                if (parser.squeezeBlank && line.isBlank()) {
                    if (lastLineWasBlank) continue
                    lastLineWasBlank = true
                } else {
                    lastLineWasBlank = false
                }

                var processedLine = line
                if (parser.showEnds) processedLine += "$"
                if (parser.showTabs) processedLine = processedLine.replace("\t", "^I")
                if (parser.numberNonBlank && line.isNotBlank() || parser.number) processedLine = "${lineNumber++}: $processedLine"
                if (parser.showNonPrinting) {
                    processedLine = processedLine.map { char ->
                        if (char.isNonPrinting()) "^${convertNonPrinting(char)}" else char.toString()
                    }.joinToString("")
                }
                println(processedLine)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun Char.isNonPrinting(): Boolean = this.code < 32 || this.code == 127

    @OptIn(ExperimentalStdlibApi::class)
    fun convertNonPrinting(char: Char): String {
        return when (char.code) {
            in 0..31 -> "^${(char + 64).toChar()}" //　制御文字をキャレット表記に変換する
            127 -> "^?" // DELキャラクター
            else -> char.toString()
        }
    }
}

fun main(args: Array<String>) {
    val app = Cat()
    app.main(arrayOf("-tn", "-A", "data/files.txt", "data/files.txt"))
}
