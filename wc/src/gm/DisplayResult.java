package gm;

import java.util.ArrayList;
import java.util.List;

public class DisplayResult {
    private ParseOption option;
    private long lines = 0;
    private long words = 0;
    private long bytes = 0;
    private long characters = 0;
    private long maxLineLength = 0;

    /**
     * 解析結果を表示する処理クラスのコンストラクタ。
     *
     * @param option アプリケーションの標準オプション {@code gm.ParseOption}
     */
    public DisplayResult(ParseOption option) {
        this.option = option;
    }

    /**
     * アプリケーションの標準オプションに従い、解析結果を表示する
     *
     * @param fileAnalyzer アプリケーションの標準オプション {@code gm.FileAnalyzer}
     */
    public void printAndAggregateResult(FileAnalyzer fileAnalyzer) {
        this.print(fileAnalyzer);
        this.aggregateResult(fileAnalyzer);
    }

    /**
     * 複数ファイルがある場合、アプリケーションの標準オプションに従い、合計の解析結果を表示する
     */
    public void printTotalIfManyFiles() {
        List<String> parts = new ArrayList<>();
        if (option.filePaths.size() > 1) {
            if (option.countLines) {
                parts.add(String.valueOf(this.lines));
            }
            if (option.countWords) {
                parts.add(String.valueOf(this.words));
            }
            if (option.countCharacters) {
                parts.add(String.valueOf(this.characters));
            }
            if (option.countBytes) {
                parts.add(String.valueOf(this.bytes));
            }
            if (option.findMaxLineLength) {
                parts.add(String.valueOf(this.maxLineLength));
            }
            parts.add("total");

            printResultLine(parts);
        }
    }

    private void print(FileAnalyzer fileAnalyzer) {
        if (fileAnalyzer.errorMessage != null) {
            System.out.println(fileAnalyzer.errorMessage);
        }
        List<String> parts = new ArrayList<>();
        if (option.countLines) {
            parts.add(String.valueOf(fileAnalyzer.lines));
        }
        if (option.countWords) {
            parts.add(String.valueOf(fileAnalyzer.words));
        }
        if (option.countCharacters) {
            parts.add(String.valueOf(fileAnalyzer.characters));
        }
        if (option.countBytes) {
            parts.add(String.valueOf(fileAnalyzer.bytes));
        }
        if (option.findMaxLineLength) {
            parts.add(String.valueOf(fileAnalyzer.maxLineLength));
        }
        parts.add(fileAnalyzer.filePath);

        printResultLine(parts);
    }

    private void printResultLine(List<String> parts) {
        System.out.println(String.join(" ", parts));
    }

    private void aggregateResult(FileAnalyzer fileAnalyzer) {
        this.lines += fileAnalyzer.lines;
        this.words += fileAnalyzer.words;
        this.bytes += fileAnalyzer.bytes;
        this.characters += fileAnalyzer.characters;
        this.maxLineLength = Math.max(this.maxLineLength, fileAnalyzer.maxLineLength);
    }
}
