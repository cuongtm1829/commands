import java.io.File
import java.nio.file.StandardWatchEventKinds
import java.nio.file.FileSystems

class SimpleArgParser(private val args: Array<String>) {
    fun hasOption(option: String): Boolean = args.contains(option)
    fun parsePositionalArgument(): String? = args.find { !it.startsWith("-") }
}

class Main() {
    fun main(args: Array<String>) {
        val parser = SimpleArgParser(args)
        val filePath = parser.parsePositionalArgument()
        if (filePath != null) {
            val file = File(filePath)
            if (file.exists()) {
                watchFileIfNeeded(file, parser.hasOption("-u")) {
                    printFileContent(file, parser)
                }
            } else {
                println("File does not exist: $filePath")
            }
        } else {
            println("No file specified.")
        }
    }

    fun watchFileIfNeeded(file: File, watch: Boolean, onChange: () -> Unit) {
        if (watch) {
            val watchService = FileSystems.getDefault().newWatchService()
            val path = file.toPath().parent
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)
            Thread {
                var key = watchService.take()
                while (key != null) {
                    for (event in key.pollEvents()) {
                        val changed = event.context() as? java.nio.file.Path
                        if (file.toPath().endsWith(changed)) {
                            onChange()
                        }
                    }
                    key.reset()
                    key = watchService.take()
                }
            }.start()
        } else {
            onChange()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun printFileContent(file: File, parser: SimpleArgParser) {
        file.useLines { lines ->
            var lineNumber = 1
            lines.forEach { line ->
                var processedLine = line
                if (parser.hasOption("-E") || parser.hasOption("-e") || parser.hasOption("-A")) processedLine += "$"
                if (parser.hasOption("-T") || parser.hasOption("-A")) processedLine = processedLine.replace("\t", "^I")
                if (parser.hasOption("-n")) processedLine = "${lineNumber++}: $processedLine"
                if (parser.hasOption("-v") || parser.hasOption("-e") || parser.hasOption("-A")) {
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
    app.main(args)
}
