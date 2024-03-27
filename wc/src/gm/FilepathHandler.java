package gm;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilepathHandler {

    /**
     * 指定された入力文字列に対してファイルの検索を行い、検索結果を返す。
     * 入力文字列はglobパターンである場合、それに応じてファイルを抽出します。Globパターンでない場合
     * 入力文字列をリスト内の単一の要素として返します。
     *
     * @param rawFilePath Globパータンである可能性があるロールファイルパス。
     * @return Globパターンにマッチするファイルパス一覧です。Globパータンではない場合、rawFilePath自体
     */
    private static List<String> findFilesWithGlobPattern(String rawFilePath) {
        List<String> result = new ArrayList<>();

        try {
            PathMatcher matcher;
            Path path = Paths.get(rawFilePath);
            Path basePath;
            String pattern;

            // Globパターンであるかどうかをチェックする
            if (rawFilePath.contains("*") || rawFilePath.contains("?") || rawFilePath.contains("[") || rawFilePath.contains("{")) {
                if(path.isAbsolute()){
                    basePath = path.getParent();
                    pattern = "" + path.getFileName();
                } else {
                    basePath = Paths.get(".") ;
                    pattern = rawFilePath;
                }

                matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);

                try (Stream<Path> stream = Files.walk(basePath)) {
                    List<String> matches = stream
                            .filter(Files::isRegularFile)
                            .map(basePath::relativize)
                            .filter(matcher::matches)
                            .map(foundPath-> basePath.resolve(foundPath).toString())
                            .collect(Collectors.toList());

                    if (!matches.isEmpty()) {
                        return matches;
                    }
                }
            }
            // Globパータンではない場合、ローファイルパスをそのまま返す
            result.add(rawFilePath);
        } catch (Throwable ex) {
            result.add(rawFilePath);
        }
        return result;
    }

    public static List<String> findFiles(List<String> rawFilePaths) {
        List<String> result = new ArrayList<>();
        for(String rawFilePath : rawFilePaths) {
            result.addAll(findFilesWithGlobPattern(rawFilePath));
        }
        return result;
    }
}
