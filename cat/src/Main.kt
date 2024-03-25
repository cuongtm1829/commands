import java.io.File
import java.nio.file.StandardWatchEventKinds
import java.nio.file.FileSystems

//class SimpleArgParser(private val args: Array<String>) {
//    fun hasOption(option: String): Boolean = args.contains(option)
//    fun parsePositionalArgument(): String? = args.find { !it.startsWith("-") }
//}

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
            else -> errorMessage = "Unknown option: -$option"
        }
    }

    fun parseOptions(options: Array<String>) {
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
                            else -> errorMessage = "Unknown option: -$option"
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

        // -bは-nを上書きする
        if (numberNonBlank) {
            number = false
        }
    }
}


class Main() {
    fun main(args: Array<String>) {
        val parser = OptionsParser()
        parser.parseOptions(args)

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

    @OptIn(ExperimentalStdlibApi::class)
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
                        if ((char in '\u0000'..'\u001F' || char in '\u007F'..'\u009F') && char != '\t' && char != '\n') "^${char.code + 64}" else char.toString()
                    }.joinToString("")
                }
                println(processedLine)
            }
        }
    }
}

fun main(args: Array<String>) {
    val app = Main()
    app.main(arrayOf("-tn", "-e", "data/data.txt"))
}
