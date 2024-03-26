# Cat Command Line Tool - Kotlin Implementation

このリポジトリには、Unix コマンド ライン ツール「cat」の Kotlin 実装が含まれています。 「cat」ツールは、テキストファイルの内容を表示したり、ファイルを連結させたりするコマンドラインプログラムです。

## 機能

- ファイルの内容を表示する

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
   kotlinc src/*.kt -include-runtime -d out/Cat.jar
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

## 工夫した点

### 保守しやすいクラス構成をしている。

- Catプログラムは以下のクラスから構成されるので、複雑な要件が発生するときに、専用のファイルで修正すればよい。　　

   ParseOption：入力のテキストからアプリケーションの標準オプションを抽出するクラス。　　

   Cat：メインクラスです。ParseOptionを使って、オプションを抽出し、オプションにしたがって、ファイルを表示する
　　
### 例外をキャッチし、処理をできるだけすすめるようする。
- 実行中に例外が発生しても、プログラムが終了することなく進めるために、例外をキャッチし、エラーメッセージでユーザーに通知する。

### コードを読みやすいにする
- コードを読みやすいため、変数の名前をつけている。
- わかりにくい文字をコンスタントとして宣言し、使っている：例：`const val DEL_CHARACTER = "^?"`


