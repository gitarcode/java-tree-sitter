package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.ParsingException;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

class ParserTest extends TestBase {

    @TempDir
    private static Path tmp;
    private static Path tmpFile;

    private static Parser parser;

    private static final String source = "print(\"hi\")\n";

    @BeforeAll
    static void beforeAll() throws IOException {
        tmpFile = Files.createFile(tmp.resolve("print.py"));
        Files.writeString(tmpFile, source);
        parser = new Parser(Language.PYTHON);
    }

    @AfterAll
    static void afterAll() {
        parser.close();
    }

    @Test
    void testParseString() {
        @Cleanup Tree tree = parser.parse(source);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("module", root.getType());
        Range range = root.getRange();
        Point start = range.getStartPoint();
        Point end = range.getEndPoint();
        Assertions.assertEquals(new Point(0, 0), start);
        Assertions.assertEquals(new Point(1, 0), end);
    }

    @Test
    void testParseFile() {
        @Cleanup Tree tree = parser.parse(tmpFile);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("module", root.getType());
        Range range = root.getRange();
        Point start = range.getStartPoint();
        Point end = range.getEndPoint();
        Assertions.assertEquals(new Point(0, 0), start);
        Assertions.assertEquals(new Point(1, 0), end);
    }

    @Test
    void testParserSetLanguage() {
        @Cleanup Parser parser = new Parser(Language.PYTHON);
        parser.setLanguage(Language.JAVA);
        String source = "public class _ {}\n";
        @Cleanup Tree tree = parser.parse(source);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("program", root.getType());
        Range range = root.getRange();
        Point start = range.getStartPoint();
        Point end = range.getEndPoint();
        Assertions.assertEquals(new Point(0, 0), start);
        Assertions.assertEquals(new Point(1, 0), end);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    @SneakyThrows(URISyntaxException.class)
    void testParserTimeout() {
        @Cleanup Parser parser = new Parser(Language.JAVA);
        Assertions.assertEquals(0, parser.getTimeout());
        parser.setTimeout(10);
        Assertions.assertEquals(10, parser.getTimeout());
        Path path = Path.of(getClass().getClassLoader().getResource("deep_string_concat").toURI());
        Assertions.assertThrows(ParsingException.class, () -> parser.parse(path));
        TimeUnit unit = TimeUnit.SECONDS;
        parser.setTimeout(1, unit);
        Assertions.assertEquals(unit.toMicros(1), parser.getTimeout());
        Assertions.assertFalse(parser.parse(path).isNull());
        Duration duration = Duration.ofSeconds(1);
        parser.setTimeout(duration);
        Assertions.assertEquals(duration.toMillis() * 1000, parser.getTimeout());
        Assertions.assertFalse(parser.parse(path).isNull());
        parser.setTimeout(Duration.ofNanos(500));
        Assertions.assertEquals(0, parser.getTimeout());
        parser.setTimeout(500, TimeUnit.NANOSECONDS);
        Assertions.assertEquals(0, parser.getTimeout());
    }

    @Test
    @SuppressWarnings("resource")
    void testParserThrows() {
        Assertions.assertThrows(NullPointerException.class, () -> new Parser(null));
        Assertions.assertThrows(UnsatisfiedLinkError.class, () -> new Parser(Language._INVALID_));
        Assertions.assertThrows(NullPointerException.class, () -> parser.setTimeout(null));
        Assertions.assertThrows(NullPointerException.class, () -> parser.setTimeout(100, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.setTimeout(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.setTimeout(-1, TimeUnit.MICROSECONDS));
    }
}
