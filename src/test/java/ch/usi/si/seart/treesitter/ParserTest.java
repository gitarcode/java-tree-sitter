package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.parser.ParsingException;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

class ParserTest extends BaseTest {

    @TempDir
    private static Path tmp;
    private static Path tmpFile;

    private static Parser parser;

    private static final String source = "print(\"hi\")\n";

    @BeforeAll
    static void beforeAll() throws IOException {
        tmpFile = Files.createFile(tmp.resolve("print.py"));
        Files.writeString(tmpFile, source);
        parser = Parser.getFor(Language.PYTHON);
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
        checkRange(root);
    }

    @Test
    void testParseFile() {
        @Cleanup Tree tree = parser.parse(tmpFile);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("module", root.getType());
        checkRange(root);
    }

    @Test
    void testSetLanguage() {
        @Cleanup Parser parser = Parser.builder().language(Language.PYTHON).build();
        parser.setLanguage(Language.JAVA);
        String source = "public class _ {}\n";
        @Cleanup Tree tree = parser.parse(source);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("program", root.getType());
        checkRange(root);
    }

    private void checkRange(Node node) {
        Range range = node.getRange();
        Point start = range.getStartPoint();
        Point end = range.getEndPoint();
        Assertions.assertEquals(_0_0_, start);
        Assertions.assertEquals(_1_0_, end);
    }

    @Test
    void testSetIncludedRanges() {
        List<Range> ranges;
        ranges = parser.getIncludedRanges();
        Assertions.assertTrue(ranges.isEmpty());
        Range range = new Range(0, 1, _0_0_, _1_0_);
        parser.setIncludedRanges(range);
        ranges = parser.getIncludedRanges();
        Assertions.assertFalse(ranges.isEmpty());
        Assertions.assertEquals(1, ranges.size());
        Range copy = ranges.stream().findFirst().orElseGet(Assertions::fail);
        Assertions.assertEquals(range, copy);
        parser.setIncludedRanges();
        ranges = parser.getIncludedRanges();
        Assertions.assertTrue(ranges.isEmpty());
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    @SneakyThrows(URISyntaxException.class)
    void testSetTimeout() {
        @Cleanup Parser parser = Parser.getFor(Language.JAVA);
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
    }

    @Test
    void testSetTimeoutNanoseconds() {
        Assertions.assertEquals(0, parser.getTimeout());
        parser.setTimeout(Duration.ofNanos(500));
        Assertions.assertEquals(0, parser.getTimeout());
        parser.setTimeout(500, TimeUnit.NANOSECONDS);
        Assertions.assertEquals(0, parser.getTimeout());
    }

    @Test
    void testBuilder() {
        Duration duration = Duration.ofSeconds(3);
        @Cleanup Parser parser = Parser.builder()
                .language(Language.JAVA)
                .timeout(duration)
                .build();
        Assertions.assertFalse(parser.isNull());
        Assertions.assertEquals(duration.toMillis() * 1000, parser.getTimeout());
    }

    @Test
    void testToBuilder() {
        Parser.Builder builder = parser.toBuilder();
        @Cleanup Parser other = builder.language(Language.JAVA)
                .timeout(Duration.ofSeconds(1))
                .build();
        Assertions.assertFalse(other.isNull());
        Assertions.assertNotEquals(parser, other);
        Assertions.assertNotEquals(parser.getTimeout(), other.getTimeout());
        Assertions.assertNotEquals(parser.getLanguage(), other.getLanguage());
    }

    private static class ConstructorExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null),
                    Arguments.of(UnsatisfiedLinkError.class, invalid)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(ConstructorExceptionProvider.class)
    void testBuilderThrows(Class<Throwable> throwableType, Language language) {
        Assertions.assertThrows(throwableType, () -> Parser.builder().language(language));
    }

    @Test
    void testBuildThrows() {
        Parser.Builder builder = Parser.builder();
        Assertions.assertThrows(NullPointerException.class, builder::build);
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(ConstructorExceptionProvider.class)
    void testSetLanguageThrows(Class<Throwable> throwableType, Language language) {
        Assertions.assertThrows(throwableType, () -> parser.setLanguage(language));
    }

    private static class SetIncludedRangesExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            Range[] array = new Range[]{ null };
            List<Range> list = Arrays.asList(array);
            Executable callArraySetter = () -> parser.setIncludedRanges(array);
            Executable callListSetter = () -> parser.setIncludedRanges(list);
            return Stream.of(
                    Arguments.of(NullPointerException.class, callArraySetter),
                    Arguments.of(NullPointerException.class, callListSetter)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(SetIncludedRangesExceptionProvider.class)
    void testSetIncludedRangesThrows(Class<Throwable> throwableType, Executable executable) {
        Assertions.assertThrows(throwableType, executable);
    }

    private static class SetTimeoutExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null),
                    Arguments.of(IllegalArgumentException.class, -1L)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(SetTimeoutExceptionProvider.class)
    void testSetTimeoutThrows(Class<Throwable> throwableType, Long timeout) {
        Assertions.assertThrows(throwableType, () -> parser.setTimeout(timeout));
        Assertions.assertThrows(throwableType, () -> Parser.builder().timeout(timeout));
    }

    private static class SetTimeoutWitTimeUnitExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, 100L, null),
                    Arguments.of(IllegalArgumentException.class, -1L, TimeUnit.MICROSECONDS)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(SetTimeoutWitTimeUnitExceptionProvider.class)
    void testSetTimeoutThrows(Class<Throwable> throwableType, Long timeout, TimeUnit timeUnit) {
        Assertions.assertThrows(throwableType, () -> parser.setTimeout(timeout, timeUnit));
        Assertions.assertThrows(throwableType, () -> Parser.builder().timeout(timeout, timeUnit));
    }
}
