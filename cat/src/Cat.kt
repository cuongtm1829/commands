import java.io.File

/**
 * メイン処理クラス
 * Catコマンドの基本的な機能を模倣し、ファイルシステムからテキストファイルの内容を取得して表示するメソッドを含んでいます。
 */
class Cat() {
    companion object {
        const val DEL_CHARACTER = "^?"
    }

    fun main(args: Array<String>) {
        val option = ParseOption()
        option.parseOptions(args)

        if(option.errorMessage != null) {
            println(option.errorMessage)
            return
        }

        if(option.help) {
            showHelp();
            return;
        }

        if(option.version) {
            showVersion();
            return;
        }

        val filePaths = option.filePaths
        // ファイルパスが渡さないときに、Standard Inputからデータを読み込む
        val isStandardIn = filePaths.size == 0;
        if(isStandardIn) {
            val lineNumber = MutableRef(1)
            val lastLineWasBlank = MutableRef(false)
            while (true) {
                val line = readLine() ?: break
                printLineContent(line, option, lastLineWasBlank, lineNumber)
            }

        } else {
            for (filePath in filePaths) {
                val file = File(filePath)
                if (file.exists()) {
                    try {
                        printFileContent(file, option)
                    } catch (ex: Exception) {
                        println("$filePath: Unknown error when reading file")
                    }
                } else {
                    println("$filePath: No such file or directory")
                }
            }
        }
    }

    private fun printFileContent(file: File, parser: ParseOption) {
        file.useLines { lines ->
            val lineNumber = MutableRef(1)
            val lastLineWasBlank = MutableRef(false)

            for (line in lines) {
                if(lastLineWasBlank.value) continue
                printLineContent(line, parser, lastLineWasBlank, lineNumber)

            }
        }
    }

    private fun printLineContent(line: String, parser: ParseOption, lastLineWasBlank: MutableRef<Boolean>, lineNumber: MutableRef<Int>) {
        if (parser.squeezeBlank && line.isBlank()) {
            if (lastLineWasBlank.value) return
            lastLineWasBlank.value = true
        } else {
            lastLineWasBlank.value = false
        }

        var processedLine = line
        if (parser.showEnds) processedLine += "$"
        if (parser.showTabs) processedLine = processedLine.replace("\t", "^I")
        if (parser.numberNonBlank && line.isNotBlank() || parser.number) processedLine = "${lineNumber.value++}: $processedLine"
        if (parser.showNonPrinting) {
            processedLine = processedLine.map { char ->
                if (char.isNonPrinting()) "^${convertNonPrinting(char)}" else char.toString()
            }.joinToString("")
        }
        println(processedLine)
    }

    /**
     * 文字が制御文字であるかどうかをチェックする。
     * 制御文字とは、ASCIIコードで32未満の文字または127（DEL）の文字のことを指す。
     * この関数は、文字が制御文字である場合にtrueを返し、そうでない場合はfalseを返す。
     *
     * @return Boolean 文字が制御文字であればtrue、そうでなければfalse。
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun Char.isNonPrinting(): Boolean = this.code < 32 || this.code == 127

    /**
     * 指定された文字が制御文字またはDELキャラクターである場合、可視化するためのキャレット表記に変換します。
     * ASCIIの制御文字（コードポイントが0から31まで）は、それぞれに64を加えたASCII文字にキャレット(^)を前置して表示します。
     * DELキャラクター（コードポイント127）は"^?"に変換されます。それ以外の文字はそのままの形で返されます。
     *
     * @param char 変換対象の文字。
     * @return 変換後の文字列。
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun convertNonPrinting(char: Char): String {
        return when (char.code) {
            in 0..31 -> "^${(char + 64).toChar()}" //　制御文字をキャレット表記に変換する
            127 -> DEL_CHARACTER
            else -> char.toString()
        }
    }

    private fun showHelp() {
        println("""NAME
       cat - concatenate files and print on the standard output

SYNOPSIS
       cat [OPTION]... [FILE]...

DESCRIPTION
       Concatenate FILE(s) to standard output.

       With no FILE, read standard input.
       -A, --show-all
              equivalent to -vET
       -b, --number-nonblank
              number nonempty output lines, overrides -n
       -e     equivalent to -vE
       -E, --show-ends
              display ${'$'} at end of each line
       -n, --number
              number all output lines
       -s, --squeeze-blank
              suppress repeated empty output lines
       -t     equivalent to -vT
       -T, --show-tabs
              display TAB characters as ^I
       -u     (ignored)
       -v, --show-nonprinting
              use ^ and M- notation, except for LFD and TAB
       --help display this help and exit
       --version
              output version information and exit
""")
    }

    private fun showVersion() {
        println("wc (GMS) version 1.0")
    }
}

class ParseOption {
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

    /**
     * 文字列であるコマンドラインオプションから標準的なアプリケーションオプションを作成する。
     * @param options 文字列の配列。各文字列はコマンドラインオプション。
     */
    fun parseOptions(options: Array<String>) {
        if(options.contains("--help")) {
            help = true
            return
        }

        if(options.contains("--version")) {
            version = true
            return
        }
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

class MutableRef<T>(var value: T)

fun main(args: Array<String>) {
    val app = Cat()
    app.main(args)
}