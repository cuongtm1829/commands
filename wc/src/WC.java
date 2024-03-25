import java.util.Arrays;

public class WC {
    public static void main(String[] args) {
        if (args.length == 0 || Arrays.asList(args).contains("--help")) {
            showHelp();
            return;
        }

        if (Arrays.asList(args).contains("--version")) {
            showVersion();
            return;
        }
        run(args);
    }

    private static void run(String[] args) {
        ParseOption option = new ParseOption(args);
        if(option.errorMessage != null) {
            System.out.println(option.errorMessage);
            return;
        }

        DisplayResult displayResult = new DisplayResult(option);

        for (String path : option.filePaths) {
            FileAnalyzer result = new FileAnalyzer(option, path);
            displayResult.printAndAggregateResult(result);
        }

        displayResult.printTotalIfManyFiles();
    }

    private static void showHelp() {
        System.out.println("Usage: wc [OPTION]... [FILE]...\n" +
                "  or:  wc [OPTION]... --files0-from=F\n" +
                "Print newline, word, and byte counts for each FILE, and a total line if\n" +
                "more than one FILE is specified.  A word is a non-zero-length sequence of\n" +
                "characters delimited by white space.\n" +
                "\n" +
                "With no FILE, or when FILE is -, read standard input.\n" +
                "\n" +
                "The options below may be used to select which counts are printed, always in\n" +
                "the following order: newline, word, character, byte, maximum line length.\n" +
                "  -c, --bytes            print the byte counts\n" +
                "  -m, --chars            print the character counts\n" +
                "  -l, --lines            print the newline counts\n" +
                "      --files0-from=F    read input from the files specified by\n" +
                "                           NUL-terminated names in file F;\n" +
                "                           If F is - then read names from standard input\n" +
                "  -L, --max-line-length  print the maximum display width\n" +
                "  -w, --words            print the word counts\n" +
                "      --help     display this help and exit\n" +
                "      --version  output version information and exit\n");
    }

    private static void showVersion() {
        System.out.println("wc (GMS) version 1.0");
    }
}
