# Cat Command Line Tool - Kotlin Implementation

このリポジトリには、Unix コマンド ライン ツール「cat」の Kotlin 実装が含まれています。 「cat」ツールは、テキストファイルの内容を表示したり、ファイルを連結させたりするコマンドラインプログラムです。

## 機能
この実装はファイルパスがglobパターンの時に以外に、UnixコマンドラインCatの以下の機能を実装している。

- 以下のオプションに従って、ファイルの内容を表示すルコとができる  
    -A, --show-all：-vETと同等  
    -b, --number-nonblank：空でない出力行に番号を付ける、-nを上書き  
    -e：-vEと同等  
    -E, --show-ends：各行の末尾に$を表示  
    -n, --number：すべての出力行に番号を付ける  
    -s, --squeeze-blank：繰り返される空の出力行を抑制  
    -t：-vTと同等  
    -T, --show-tabs：TAB文字を^Iとして表示  
    -u：(無視される)  
    -v, --show-nonprinting：LFDとTABを除き、^とM-表記を使用  
- 複数ファイルを結合し、表示まは、出力できる。コマンド引数例： file1.txt file2.txt　> out.txt
- 既存のファイルに新しい内容を追加することもできる。コマンド引数例： file1.txt file2.txt　>> out.txt
- BinaryファイルをCharとして表示できる
- ファイルパスを指定しない場合、標準的な入力を表示できる。例：find *.txt | java -jar Cat.jar -n
- globパターンのファイル名（globパターンのフォルダ名をサポートしない）をサポートする。例：data/*.txt (*/*.txtをサポートしない)

## 使用ガイド

### 前提条件

- Java Development Kit (JDK) はインストールされている（Java 8以上）
- Kotlinコンパイラ
- 表示テキストファイル（dataフォルダーにサンプルがある）

### 確認方法

1. レポジトリーをクローンする.
   ```bash
   git clone {GMS repo}
   ```
2. プロジェクトのフォルダーへ移動する
   ```bash
   cd cat
   ```
3. Javaファイルソースコードをコンパイルする
   ```bash
   kotlinc src/main/kotlin/vn/gmgroup/cat/*.kt -include-runtime -d out/Cat.jar
   ```
4. outフォルダーへ移動し、分析ファイルを指定し、プログラムを実行する
   ```bash
   cd out
   java -jar Cat.jar -n /path/to/your/file.txt
   ```
   例：
   ```bash
   cd out
   java -jar Cat.jar ../data/files.txt -n
   ```

## テスト方法：
1. テスト実施のため、Gradlewを使用する
```shell
./gradlew test
```

## 工夫した点

### 保守しやすいクラス構成をしている。

- Catプログラムは以下のクラスから構成されるので、複雑な要件が発生するときに、専用のファイルで修正すればよい。　　

   ParseOption：入力のテキストからアプリケーションの標準オプションを抽出するクラス。　　

   FilepathHandler：フィル名がGlobパターンであるサポートするようにファイル文字列からファイルパスを抽出する
                 例：ファイ名のみではなく、フォルダ名がGlobパターンであることもサポートする

   Cat：メインクラスです。ParseOptionを使って、オプションを抽出し、オプションにしたがって、ファイルを表示する
　　
### 例外をキャッチし、処理をできるだけすすめるようする。
- 実行中に例外が発生しても、プログラムが終了することなく進めるために、例外をキャッチし、エラーメッセージでユーザーに通知する。

### コードを読みやすいにする
- コードを読みやすいため、変数の名前をつけている。
- わかりにくい文字をコンスタントとして宣言し、使っている：例：`const val DEL_CHARACTER = "^?"`


