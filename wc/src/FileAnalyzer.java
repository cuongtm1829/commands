import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;

public class FileAnalyzer {
    public long lines = 0;
    public long words = 0;
    public long bytes = 0;
    public long characters = 0;
    public long maxLineLength = 0;
    public String filePath;
    public String errorMessage;

    /**
     * アプリケーションの標準化オプションとファイルパスを渡し、オプションの指定に従う解析処理を行う
     * @param option アプリケーションの標準化オプション {@code ParseOption}
     * @param filePathIn 解析されるファイルのパス {@code String}
     */
    public FileAnalyzer(ParseOption option, String filePathIn) {
        this.filePath = filePathIn;
        File file = new File(filePath);
        if (file.isDirectory()) {
            errorMessage = filePath + " is a directory.";
            return;
        }

        Path path = Paths.get(this.filePath);
        try {
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

        } catch (IOException e) {
            if(e instanceof FileNotFoundException || e instanceof NoSuchFileException) {
                errorMessage = "The file or folder was not found: " + this.filePath;
            } else {
                errorMessage = "Unknown error processing file: " + this.filePath + "\n";
                errorMessage += e.getMessage();
            }
        }
    }
    private long countLines(Path path) throws IOException {
        return Files.lines(path).count();
    }

    private long countWords(Path path) throws IOException {
        return Files.lines(path)
                .flatMap(line -> Arrays.stream(line.split("\\s+")))
                .count();
    }

    private long countBytes(Path path) throws IOException {
        return Files.size(path);
    }

    private long countCharacters(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8).length();
    }

    private int maxLineLength(Path path) throws IOException {
        return Files.lines(path)
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }
}

