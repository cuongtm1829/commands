# WC Command Line Tool - Java Implementation

This repository contains Java implementation of the Unix command line tool 'wc'. The 'wc' tool is commonly used to count the number of lines, words, and characters in a file. With this Java version, you can easily analyze text files and obtain valuable insights about their contents.

## Features

- Count the number of lines in a file
- Count the number of words in a file
- Count the number of characters (with and without spaces) in a file
- Identify the longest line in a file

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed on your machine
- Text file(s) to analyze

### Usage

1. Clone this repository to your local machine.
   ```bash
   git clone https://github.com/your-username/wc-command-line-tool.git
   ```
2. Navigate to the project directory.
   ```bash
   cd wc-command-line-tool
   ```
3. Compile the Java source code.
   ```bash
   javac WC.java
   ```
4. Run the program and provide the path to the text file(s) you want to analyze.
   ```bash
   java WC /path/to/your/file.txt
   ```
### Example
Let's say we have a text file called sample.txt with the following content:

  ```text
    Copy code
    Hello, world!
    This is a sample text file.
    It contains multiple lines.
  ```

Running the command:
  ```bash
  java WC sample.txt
  ```
The output will be:

![Screenshot](https://github.com/Ketan-Pal/wc-command-line-tool/blob/main/Screenshot.jpg?raw=true)
