/*
 * Copyright (c) Artnet Gmsolution. All rights reserved.
 *
 * アプリケーションの標準的なオプションを抽出するクラス
 */
package vn.gmgroup.cat

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
            'u' -> {
            } // 無視
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
        if (options.contains("--help")) {
            help = true
            return
        }

        if (options.contains("--version")) {
            version = true
            return
        }
        try {
            val rawFilePaths: MutableList<String> = mutableListOf()
            options.forEach { option ->
                if (!option.startsWith("-")) {
                    rawFilePaths.add(option)
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

            filePaths.addAll(FilepathHandler.searchFilesByMultipleGlobPatterns(rawFilePaths.toTypedArray()))
        } catch (e: Exception) {
            errorMessage = e.message;
        }

        // -bは-nを上書きする
        if (numberNonBlank) {
            number = false
        }
    }
}
