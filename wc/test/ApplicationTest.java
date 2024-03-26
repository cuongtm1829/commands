package test;
import org.junit.jupiter.api.Test;

import gm.WC;

public class ApplicationTest {
    @Test
    public void testCountLinesAdnWords() {
        String[] args = {"-lw", "data/data.txt"};
        WC.main(args);
    }

    @Test
    public void testReadingInputFromFile() {
        String[] args = {"--files0-from", "data/files.txt"};
        WC.main(args);
    }

    @Test
    public void testPassOptionUsingEqualSign() {
        String[] args = {"--files0-from=data/files.txt"};
        WC.main(args);
    }
}
