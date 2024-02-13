package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

class LanguageTest extends BaseTest {

    @TempDir
    private static Path tmp;
    private static final Language language = Language.PYTHON;

    @ParameterizedTest
    @EnumSource(Language.class)
    void testValidate(Language language) {
        Assertions.assertDoesNotThrow(() -> Language.validate(language));
    }

    private static class ValidateExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null),
                    Arguments.of(UnsatisfiedLinkError.class, invalid)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(ValidateExceptionProvider.class)
    void testValidateThrows(Class<Throwable> throwableType, Language language) {
        Assertions.assertThrows(throwableType, () -> Language.validate(language));
    }

    private static class AssociatedWithProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of("requirements.txt", List.of()),
                    Arguments.of(".py", List.of(Language.PYTHON)),
                    Arguments.of(".gitattributes", List.of(Language.GITATTRIBUTES)),
                    Arguments.of("__init__.py", List.of(Language.PYTHON)),
                    Arguments.of(".gitignore", List.of(Language.GITIGNORE)),
                    Arguments.of("Main.java", List.of(Language.JAVA)),
                    Arguments.of("Dockerfile", List.of(Language.DOCKERFILE)),
                    Arguments.of("example.h", List.of(
                            Language.C,
                            Language.CPP,
                            Language.OBJECTIVE_C
                    ))
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(AssociatedWithProvider.class)
    void testAssociatedWith(String name, List<Language> expected) {
        Path path = Path.of(tmp.toString(), name);
        Collection<Language> actual = Language.associatedWith(path);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> actual.add(null));
    }

    private static class AssociatedWithExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null),
                    Arguments.of(IllegalArgumentException.class, tmp)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(AssociatedWithExceptionProvider.class)
    void testAssociatedWithThrows(Class<Throwable> throwableType, Path path) {
        Assertions.assertThrows(throwableType, () -> Language.associatedWith(path));
    }

    @Test
    void testNextState() {
        @Cleanup Parser parser = Parser.getFor(language);
        @Cleanup Tree tree = parser.parse("pass");
        Node root = tree.getRootNode();
        Assertions.assertEquals(0, language.nextState(root));
    }

    private static Stream<Arguments> invalidNodes() {
        return Stream.of(
                Arguments.of(NullPointerException.class, null),
                Arguments.of(IllegalArgumentException.class, empty)
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidNodes")
    void testNextStateThrows(Class<Throwable> throwableType, Node node) {
        Assertions.assertThrows(throwableType, () -> language.nextState(node));
        Assertions.assertThrows(throwableType, () -> invalid.nextState(node));
    }
}
