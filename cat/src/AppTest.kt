import org.junit.jupiter.api.Test

class AppTest {
    @Test
    fun `test something`() {
        val app = Cat()
        app.main(arrayOf("-tn", "-A", "data/files.txt", "data/files.txt"))
    }
}