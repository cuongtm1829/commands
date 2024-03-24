package com.gm;

import java.util.ArrayList;
import java.util.List;

public class DisplayResult {
    ParseOption option;
    public long lines = 0;
    public long words = 0;
    public long bytes = 0; // Added to handle bytes count
    public long characters = 0;
    public long maxLineLength = 0;
    public DisplayResult(ParseOption option) {
        this.option = option;
    }

    public void printAndAggregateResult(FileAnalyzer fileAnalyzer) {
        this.print(fileAnalyzer);
        this.aggregateResult(fileAnalyzer);
    }

    private void print(FileAnalyzer fileAnalyzer) {
        List<String> parts = new ArrayList<>();
        if (option.countLines) {
            parts.add(String.valueOf(fileAnalyzer.lines));
        }
        if (option.countWords) {
            parts.add(String.valueOf(fileAnalyzer.words));
        }
        if (option.countBytes) {
            parts.add(String.valueOf(fileAnalyzer.bytes));
        }
        if (option.countCharacters) {
            parts.add(String.valueOf(fileAnalyzer.characters));
        }
        if (option.findMaxLineLength) {
            parts.add(String.valueOf(fileAnalyzer.maxLineLength));
        }
        parts.add(fileAnalyzer.filePath);
        System.out.println(String.join(" ", parts));
    }

    private void aggregateResult(FileAnalyzer fileAnalyzer) {
        this.lines += fileAnalyzer.lines;
        this.words += fileAnalyzer.words;
        this.bytes += fileAnalyzer.bytes;
        this.characters += fileAnalyzer.characters;
        this.maxLineLength = this.maxLineLength > fileAnalyzer.maxLineLength ? this.maxLineLength : fileAnalyzer.maxLineLength;
    }

    public void printTotalIfNeeded() {
        List<String> parts = new ArrayList<>();
        if(option.filePaths.size() > 1) {
            if (option.countLines) {
                parts.add(String.valueOf(this.lines));
            }
            if (option.countWords) {
                parts.add(String.valueOf(this.words));
            }
            if (option.countBytes) {
                parts.add(String.valueOf(this.bytes));
            }
            if (option.countCharacters) {
                parts.add(String.valueOf(this.characters));
            }
            if (option.findMaxLineLength) {
                parts.add(String.valueOf(this.maxLineLength));
            }
            parts.add("total");

            System.out.println(String.join(" ", parts));
        }
    }

}
