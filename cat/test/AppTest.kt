import org.junit.jupiter.api.Test

class AppTest {
    @Test
    fun `testReadingFileWithNumber`() {
        val app = Cat()
        app.main(arrayOf("-tn", "-A", "data/files.txt", "data/files.txt"))
    }
}