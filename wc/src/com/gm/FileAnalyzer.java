package com.gm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileAnalyzer {
    public long lines = 0;
    public long words = 0;
    public long bytes = 0; // Added to handle bytes count
    public long characters = 0;
    public long maxLineLength = 0;
    public String filePath;
    public FileAnalyzer(ParseOption option, String filePath) {
        this.filePath = filePath;
        Path path = Paths.get(this.filePath);

        try {
            if (option.countLines) {
                this.lines = countLines(path);
            }
            if (option.countWords) {
                this.words = countWords(path);
            }
            if (option.countBytes) {
                this.bytes = countBytes(path);
            }
            if (option.countCharacters) {
                this.characters = countCharacters(path);
            }
            if (option.findMaxLineLength) {
                this.maxLineLength = maxLineLength(path);
            }

        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
            return;
        }
    }
    public long countLines(Path path) throws IOException {
        return Files.lines(path).count();
    }

    public long countWords(Path path) throws IOException {
        return Files.lines(path)
                .flatMap(line -> Arrays.stream(line.split("\\s+")))
                .count();
    }

    public long countBytes(Path path) throws IOException {
        return Files.size(path);
    }

    public long countCharacters(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8).length();
    }

    public int maxLineLength(Path path) throws IOException {
        return Files.lines(path)
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }
}

