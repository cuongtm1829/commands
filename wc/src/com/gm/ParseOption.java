package com.gm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseOption {
    public boolean countLines = false;
    public boolean countWords = false;
    public boolean countBytes = false; // Added to handle bytes count
    public boolean countCharacters = false;
    public boolean findMaxLineLength = false;
    public boolean files0FromFlag = false;
    public List<String> filePaths = new ArrayList<>();

    public ParseOption(String[] args) {
        List<String> argsList = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
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

//        Default values
        if (!countLines && !countWords && !countCharacters && !countBytes && !findMaxLineLength) {
            countLines = true;
        }
    }

    private void parseOptions(String arg) {
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
                    System.err.println("Unknown option: " + arg);
            }
        } else {
            for (int i = 1; i < arg.length(); i++) { // Start at 1 to skip the '-'
                switch (arg.charAt(i)) {
                    case 'l':
                        countLines = true;
                        break;
                    case 'w':
                        countWords = true;
                        break;
                    case 'm':
                    case 'c': // Treat 'c' as counting characters/bytes
                        countCharacters = countBytes = true;
                        break;
                    case 'L':
                        findMaxLineLength = true;
                        break;
                    default:
                        throw new Error("unknow option" + arg.charAt(i));
//                        System.err.println("Unknown option: " + arg.charAt(i));
                }
            }
        }
    }

    private ArrayList<String> processFile0From(String args) {
        boolean readFromStdIn = args.equals("-");
        ArrayList<String> filePaths = new ArrayList<>();
        try {
            if (readFromStdIn) {
                BufferedReader stdInReader = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = stdInReader.readLine()) != null) {
                    filePaths.add(line);
                }
            } else {
                FileInputStream fis = new FileInputStream(args);
                byte[] buffer = new byte[1024];
                int length;
                StringBuilder sb = new StringBuilder();
                while ((length = fis.read(buffer)) > -1) {
                    sb.append(new String(Arrays.copyOf(buffer, length), StandardCharsets.UTF_8));
                }
                fis.close();

                // Splitting the string by null character
                String[] fileNames = sb.toString().split("\0");
                for (String fileName : fileNames) {
                    filePaths.add(fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePaths;
    }
}
