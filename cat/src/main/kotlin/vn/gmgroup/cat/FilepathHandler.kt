/*
 * Copyright (c) Artnet Gmsolution. All rights reserved.
 *
 * 文字列のファイルパスからGlobパータンを考慮し、ファイルパスを抽出するクラス
 */
package vn.gmgroup.cat

import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FilepathHandler {
    companion object {
        private fun searchFilesByGlobPattern(inputPattern: String): List<String> {
            val result: MutableList<String> = mutableListOf()

            if (inputPattern.any { it in listOf('*', '?', '[', ']') }) {

                val normalizedPattern = inputPattern.replace("\\", "/")
                val (dirPath, globPattern) = extractDirAndPattern(normalizedPattern)

                val matcher = FileSystems.getDefault().getPathMatcher("glob:$globPattern")

                val paths = Files.walk(dirPath)
                try {
                    paths
                        .filter { path ->
                            Files.isRegularFile(path) && matcher.matches(path.fileName)
                        }
                        .forEach { path ->
                            result.add(path.toAbsolutePath().toString())
                        }
                } finally {
                    paths.close()
                }
            } else {
                result.add(inputPattern)
            }

            return result
        }

        private fun extractDirAndPattern(inputPattern: String): Pair<Path, String> {
            val lastSlashIndex = inputPattern.lastIndexOf('/')
            return if (lastSlashIndex != -1) {
                val dirPart = inputPattern.substring(0, lastSlashIndex)
                val globPart = inputPattern.substring(lastSlashIndex + 1)
                Pair(Paths.get(dirPart).toAbsolutePath(), "$globPart")
            } else {
                Pair(Paths.get(".").toAbsolutePath(), "$inputPattern")
            }
        }

        fun searchFilesByMultipleGlobPatterns(patterns: Array<String>): Set<String> {
            val allFilePaths = mutableSetOf<String>()

            patterns.forEach { pattern ->
                val matchedFiles = searchFilesByGlobPattern(pattern)
                allFilePaths.addAll(matchedFiles)
            }

            return allFilePaths
        }
    }
}
