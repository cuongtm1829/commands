/*
 * Copyright (c) Artnet Gmsolution. All rights reserved.
 *
 * コマンド引数に従ってファイルの内容を表示するクラス
 */
package vn.gmgroup.cat

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

        if (option.errorMessage != null) {
            println(option.errorMessage)
            return
        }

        if (option.help) {
            showHelp();
            return;
        }

        if (option.version) {
            showVersion();
            return;
        }

        val filePaths = option.filePaths
        // ファイルパスが渡さないときに、Standard Inputからデータを読み込む
        val isStandardIn = filePaths.size == 0;
        if (isStandardIn) {
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
                printLineContent(line, parser, lastLineWasBlank, lineNumber)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun printLineContent(
        line: String,
        parser: ParseOption,
        lastLineWasBlank: MutableRef<Boolean>,
        lineNumber: MutableRef<Int>
    ) {
        if (parser.squeezeBlank && line.isBlank()) {
            if (lastLineWasBlank.value) return
            lastLineWasBlank.value = true
        } else {
            lastLineWasBlank.value = false
        }

        var processedLine = line
        if (parser.showEnds) processedLine += "$"
        if (parser.showTabs) processedLine = processedLine.replace("\t", "^I")
        if (parser.numberNonBlank && line.isNotBlank() || parser.number) processedLine =
            "${lineNumber.value++}: $processedLine"
        if (parser.showNonPrinting) {
            processedLine = processedLine.map { char ->
                convertCharAndNonPrinting(char)
            }.joinToString("")
        }
        println(processedLine)
    }


    /**
     * 指定された文字が制御文字またはDELキャラクターである場合、可視化するためのキャレット表記に変換します。
     * TABとLFD意外に、ASCIIの制御文字（コードポイントが0から31まで）は、それぞれに64を加えたASCII文字にキャレット(^)を前置して表示します。
     * 制御
     *
     * @param char 変換対象の文字。
     * @return 変換後の文字列。
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun convertCharAndNonPrinting(char: Char): String {
        return when (char.code) {
            in 0..31 -> when (char) {
                '\t' -> char.toString() // TAB
                '\n' -> char.toString() // LFD
                else -> "^${(char.code + 64).toChar()}" //制御文字
            }
            in 127..159 -> "M-${(char.code - 128).toChar()}" //制御文字
            else -> char.toString()
        }
    }

    private fun showHelp() {
        println(
"""NAME
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
"""
        )
    }

    private fun showVersion() {
        println("cat (GMS) version 1.0")
    }
}

class MutableRef<T>(var value: T)

fun main(args: Array<String>) {
    val app = Cat()
    app.main(args)
}
