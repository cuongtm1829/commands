package gm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseOption {
    public boolean countLines = false;
    public boolean countWords = false;
    public boolean countBytes = false;
    public boolean countCharacters = false;
    public boolean findMaxLineLength = false;
    public boolean files0FromFlag = false;
    public List<String> filePaths = new ArrayList<>();
    public String errorMessage = null;

    private static final String NULL_CHARACTER = "\0";

    /**
     * コマンドライン引数を解析してアプリケーションの標準オプションを構成する。
     * @param args コマンドライン引数を表す {@code String} オブジェクトの配列
     */
    public ParseOption(String[] args) {
        try {
            List<String> argsList = new ArrayList<>();
            for (String arg : args) {
                argsList.addAll(Arrays.asList(arg.split("=")));
            }

            for (int i = 0; i < argsList.size(); i++) {
                String arg = argsList.get(i);
                if (arg.equals("--files0-from")) {
                    files0FromFlag = true;
                    if (++i < argsList.size()) {
                        filePaths = this.processFile0From(argsList.get(i));
                    }
                } else if (arg.startsWith("-")) {
                    parseOptions(arg);
                } else {
                    filePaths.add(arg);
                }
            }

            //コマンドライン引数を渡さない場合、行数、ワード、バイト数を表示する
            if (!countLines && !countWords && !countCharacters && !countBytes && !findMaxLineLength) {
                countLines = countWords = countBytes = true;
            }
        } catch (Exception ex) {
            this.errorMessage = ex.getMessage();
        }

    }

    private void parseOptions(String arg) throws Exception {
        if (arg.startsWith("--")) {
            switch (arg) {
                case "--lines":
                    countLines = true;
                    break;
                case "--words":
                    countWords = true;
                    break;
                case "--chars":
                    countCharacters = true;
                    break;
                case "--bytes":
                    countBytes = true;
                    break;
                case "--max-line-length":
                    findMaxLineLength = true;
                    break;
                default:
                    throw new Exception("Unknown option" + arg);
            }
        } else {
            for (int i = 1; i < arg.length(); i++) {
                switch (arg.charAt(i)) {
                    case 'l':
                        countLines = true;
                        break;
                    case 'w':
                        countWords = true;
                        break;
                    case 'm':
                        countCharacters = true;
                        break;
                    case 'c':
                        countBytes = true;
                        break;
                    case 'L':
                        findMaxLineLength = true;
                        break;
                    default:
                        throw new Exception("invalid option" + arg.charAt(i));
                }
            }
        }
    }

    private List<String> processFile0From(String args) throws Exception {
        boolean readFromStdIn = args.equals("-") || args.equals("");
        List<String> filePaths = new ArrayList<>();
        try {
            if (readFromStdIn) {
                BufferedReader stdInReader = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = stdInReader.readLine()) != null) {
                    filePaths.add(line);
                }
            } else {
                String sb = new String(Files.readAllBytes(Paths.get(args)), StandardCharsets.UTF_8);


                // Nullキャラクターで文字列をファイルパスに分解する
                String[] fileNames = sb.split(NULL_CHARACTER);
                filePaths.addAll(new ArrayList<>(Arrays.asList(fileNames)));
            }
        } catch (IOException e) {
            if(e instanceof FileNotFoundException) {
                throw new Exception("File not found when reading input: " + args);
            } else {
                throw new Exception("Unknown error when reading input: " + args);
            }
        }
        return filePaths;
    }
}
