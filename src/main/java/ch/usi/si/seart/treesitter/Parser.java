package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.error.ABIVersionError;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Parsers are stateful objects that can be assigned a language
 * and used to produce a {@link Tree Tree} based on some source code.
 * Instances of this class <strong>can not</strong> be created
 * without an initially set language.
 *
 * @since 1.0.0
 * @author Tommy MacWilliam
 * @author Ozren Dabić
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Parser extends External {

    Language language;

    /**
     * @param language The language used for parsing
     * @throws NullPointerException
     * if the specified language is null
     * @throws UnsatisfiedLinkError
     * if the specified language has not
     * been linked to the system library
     */
    public Parser(Language language) {
        super(createIfValid(language));
        this.language = language;
    }

    /*
     * Constructor precondition for creating a parser.
     * In essence, we should never allocate memory to
     * these structures if the language:
     * - Has not been specified (i.e. is null)
     * - Has not been linked to the system library
     */
    private static long createIfValid(Language language) {
        Language.validate(language);
        long pointer = malloc();
        setLanguage(pointer, language);
        return pointer;
    }

    private static native long malloc();

    /**
     * Delete the parser, freeing all the memory that it used.
     */
    @Override
    public native void close();

    /**
     * Set the language that the parser should use for parsing.
     *
     * @param language The language used for parsing
     * @throws NullPointerException
     * if the specified language is null
     * @throws UnsatisfiedLinkError
     * if the specified language has not
     * been linked to the system library
     */
    public void setLanguage(Language language) {
        Language.validate(language);
        setLanguage(pointer, language);
    }

    private static void setLanguage(long pointer, Language language) {
        boolean success = setLanguage(pointer, language.getId());
        if (!success) throw new ABIVersionError("Language could not be assigned to parser!");
    }

    private static native boolean setLanguage(long pointer, long language);

    /**
     * Get the duration in microseconds that parsing is allowed to take.
     *
     * @return the timeout duration set for parsing, 0 if it was not set
     * @since 1.1.0
     */
    public native long getTimeout();

    /**
     * Set the maximum duration that parsing should be allowed to take before halting.
     * If parsing takes longer than this, an exception is thrown.
     * Note that the supplied duration will be rounded down
     * to 0 if the duration is expressed in nanoseconds.
     *
     * @param duration the timeout duration
     * @throws NullPointerException if the duration is {@code null}
     * @since 1.1.0
     */
    public void setTimeout(Duration duration) {
        Objects.requireNonNull(duration, "Duration must not be null!");
        long micros = duration.toMillis() * TimeUnit.MILLISECONDS.toMicros(1);
        setTimeout(micros);
    }

    /**
     * Set the maximum duration that parsing should be allowed to take before halting.
     * If parsing takes longer than this, an exception is thrown.
     * Note that the supplied duration will be rounded down
     * to 0 if the duration is expressed in nanoseconds.
     *
     * @param timeout the timeout duration amount
     * @param timeUnit the duration time unit
     * @throws NullPointerException if the time unit is {@code null}
     * @throws IllegalArgumentException if the timeout value is negative
     * @since 1.1.0
     */
    public void setTimeout(long timeout, TimeUnit timeUnit) {
        if (timeout < 0)
            throw new IllegalArgumentException("Timeout can not be negative!");
        Objects.requireNonNull(timeUnit, "Time unit must not be null!");
        long micros = timeUnit.toMicros(timeout);
        setTimeout(micros);
    }

    /**
     * Set the maximum duration in microseconds that
     * parsing should be allowed to take before halting.
     * If parsing takes longer than this, an exception is thrown.
     *
     * @param timeout the timeout in microseconds
     * @throws IllegalArgumentException if the timeout value is negative
     * @since 1.1.0
     */
    public native void setTimeout(long timeout);

    /**
     * Use the parser to parse some source code and create a syntax tree.
     *
     * @param source The source code string to be parsed
     * @return A syntax tree matching the provided source
     * @throws ch.usi.si.seart.treesitter.exception.ParsingException
     * If a parsing failure occurs (e.g. timeout)
     * @throws UnsupportedEncodingException
     * If the UTF-16LE character set is not supported
     */
    public Tree parseString(String source) throws UnsupportedEncodingException {
        byte[] bytes = source.getBytes(StandardCharsets.UTF_16LE);
        long treePointer = parseBytes(bytes, bytes.length);
        return new Tree(treePointer, language);
    }

    native long parseBytes(byte[] source, int length);

    /**
     * Use the parser to incrementally parse a changed source code string,
     * reusing unchanged parts of the tree to speed up the process.
     *
     * @param source The source code string to be parsed
     * @param oldTree The syntax tree before changes were made
     * @return A syntax tree matching the provided source
     * @throws ch.usi.si.seart.treesitter.exception.ParsingException
     * If a parsing failure occurs (e.g. timeout)
     * @throws UnsupportedEncodingException
     * If the UTF-16LE character set is not supported
     */
    public Tree parseString(String source, Tree oldTree) throws UnsupportedEncodingException {
        byte[] bytes = source.getBytes(StandardCharsets.UTF_16LE);
        long treePointer = parseBytes(bytes, bytes.length, oldTree);
        return new Tree(treePointer, language);
    }

    native long parseBytes(byte[] source, int length, Tree oldTree);

    /**
     * Use the parser to parse some source code found in a file at the specified path.
     *
     * @param path The path of the file to be parsed
     * @return A tree-sitter Tree matching the provided source
     * @throws IOException If an I/O error occurs reading from
     * the file or a malformed or unmappable byte sequence is read
     * @throws OutOfMemoryError If the file is extremely large,
     * for example larger than 2GB
     */
    public Tree parseFile(Path path) throws IOException {
        String source = Files.readString(path);
        return parseString(source);
    }

    @Override
    @Generated
    public String toString() {
        return String.format("Parser(id: %d, language: %s)", pointer, language);
    }
}
