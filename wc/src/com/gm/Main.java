package com.gm;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
//        System.out.println(Arrays.toString(args));
//        run(new String[] { "--words", "/Users/trancuong/Desktop/test.html", "--lines", "/Users/trancuong/Desktop/test.html" });
//        run(new String[] { "--help"});
//        run(new String[] { "--version"});
//        run(new String[] { "--files0-from", "/Users/trancuong/Desktop/files.txt"});
//        run(new String[] { "-lw", "/Users/trancuong/Desktop/test.html", "/Users/trancuong/Desktop/test1.html"});
        run(args);
    }
    public static void run(String[] args) {
        if (args.length == 0 || Arrays.asList(args).contains("--help")) {
            showHelp();
            return;
        }

        if (Arrays.asList(args).contains("--version")) {
            showVersion();
            return;
        }

        ParseOption option = new ParseOption(args);
        DisplayResult displayResult = new DisplayResult(option);

        for (String path : option.filePaths) {
            FileAnalyzer result = new FileAnalyzer(option, path);
            displayResult.printAndAggregateResult(result);
        }

        displayResult.printTotalIfNeeded();
    }

    private static void showHelp() {
        System.out.println("Usage: java FileStatistics [OPTION]... [FILE]...");
        // Add more help information here
    }

    private static void showVersion() {
        System.out.println("version 1.0");
    }
}
