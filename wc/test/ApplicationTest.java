package test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import vn.gmgroup.wc.WC;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

//    @BeforeEach
//    public void setUp() {
//        System.setOut(new PrintStream(outputStreamCaptor));
//    }
//
//    @AfterEach
//    public void tearDown() {
//        System.setOut(standardOut);
//    }

    @Test
    public void testOutputAllStatistic() {
        String[] args = {"-lwmcL", "data/data.txt"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("21 432 2594 2594 881 data/data.txt"),
                "Failed to analyze file");
    }

    @Test
    public void testOutputMultipleFilesStatistic() {
        String[] args = {"-lwmcL", "data/data.txt", "data/files.txt"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("22 433 2623 2623 881 total"),
                "Failed to output total line");
    }

    @Test
    public void testWhenOneNotFound() {
        String[] args = {"-lwmcL", "data/data.txt", "data/files1.txt"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("The file or folder was not found: data/files1.txt"),
                "Failed to output error for non-exits file");
    }

    @Test
    public void testCountLinesAndWords() {
        String[] args = {"-lw", "data/*.txt"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("432 data/data.txt"),
                "Failed to read file with glob pattern");
    }

    @Test
    public void testLongArgument() {
        String[] args = {"--lines", "data/data.txt"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("21 data/data.txt"),
                "Failed to handle argument with equal");
    }

    @Test
    public void testPassedArgumentNotSupport() {
        String[] args = {"-g", "data/files.txt"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("invalid option"),
                "Failed to handle argument with equal");
    }

    @Test
    public void testPassedArgumentNotSupport2() {
        String[] args = {"--line", "data/files.txt"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("invalid option"),
                "Failed to handle argument with equal");
    }


    @Test
    public void testReadingInputFromFile() {
        String[] args = {"--files0-from", "data/files.txt"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("2594 data/data.txt"),
                "Failed to read input from files");
    }

    @Test
    public void testPassOptionUsingEqualSign() {
        String[] args = {"--files0-from=data/files.txt"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("2594 data/data.txt"),
                "Failed to handle argument with equal");
    }

    @Test
    public void testBinaryFile() {
        String[] args = {"bash.exe"};
        WC.main(args);
        assertTrue(outputStreamCaptor.toString().contains("2594 data/data.txt"),
                "Failed to handle argument with equal");
    }
}
