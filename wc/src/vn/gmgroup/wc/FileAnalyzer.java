/*
 * Copyright (c) Artnet Gmsolution. All rights reserved.
 *
 * ファイルを解析するクラス
 */
package vn.gmgroup.wc;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileAnalyzer {
    public long lines = 0;
    public long words = 0;
    public long bytes = 0;
    public long characters = 0;
    public long maxLineLength = 0;
    public String filePath;
    public String errorMessage;

    private final List<Charset> CHAR_SETS =
            new ArrayList<Charset>(Arrays.asList(StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1));

    /**
     * アプリケーションの標準オプションとファイルパスを渡し、オプションの指定に従う解析処理を行う
     *
     * @param option     アプリケーションの標準オプション {@code vm.gmgroup.wc.ParseOption}
     * @param filePathIn 解析されるファイルのパス {@code String}
     */
    public FileAnalyzer(ParseOption option, String filePathIn) {
        this.filePath = filePathIn;
        File file = new File(filePath);
        if (file.isDirectory()) {
            errorMessage = filePath + " is a directory.";
            return;
        }

        try {
            Path path = Paths.get(this.filePath);

            if (option.countLines) {
                lines = countLines(path);
            }
            if (option.countWords) {
                words = countWords(path);
            }
            if (option.countBytes) {
                bytes = countBytes(path);
            }
            if (option.countCharacters) {
                characters = countCharacters(path);
            }
            if (option.findMaxLineLength) {
                maxLineLength = maxLineLength(path);
            }
        } catch (FileNotFoundException | NoSuchFileException | InvalidPathException e) {
            errorMessage = "The file or folder was not found: " + this.filePath;
        } catch (AccessDeniedException | SecurityException e) {
            errorMessage = "You do not have access to the file: " + this.filePath;
        } catch (Exception e) {
            errorMessage = "Unknown error processing file: " + this.filePath + "\n";
            errorMessage += e.getMessage();
        }
    }

    private long countLines(Path path) throws IOException {
        for (Charset charset : CHAR_SETS) {
            try {
                return Files.lines(path, charset).count();
            } catch (UncheckedIOException ex) {
            }
        }
        throw new IOException("Can not read file: " + path.toString());
    }

    private long countWords(Path path) throws IOException {
        for (Charset charset : CHAR_SETS) {
            try {
                return Files.lines(path, charset)
                        .flatMap(line -> Arrays.stream(line.split("\\s+")))
                        .count();
            } catch (UncheckedIOException ex) {
            }
        }
        throw new IOException("Can not read file: " + path.toString());
    }

    private long countBytes(Path path) throws IOException {
        return Files.size(path);
    }

    private long countCharacters(Path path) throws IOException {
        for (Charset charset : CHAR_SETS) {
            try {
                return new String(Files.readAllBytes(path), charset).length();
            } catch (UncheckedIOException ex) {
            }
        }
        throw new IOException("Can not read file: " + path.toString());
    }

    private int maxLineLength(Path path) throws IOException {
        for (Charset charset : CHAR_SETS) {
            try {
                return Files.lines(path, charset)
                        .mapToInt(String::length)
                        .max()
                        .orElse(0);
            } catch (UncheckedIOException ex) {
            }
        }
        throw new IOException("Can not read file: " + path.toString());
    }
}

