import org.junit.jupiter.api.Test

class AppTest {
    @Test
    fun `test reading_file_with_number`() {
        val app = Cat()
        app.main(arrayOf("-tn", "-A", "data/files.txt", "data/files.txt"))
    }
}