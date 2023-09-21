package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Generated;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Cursor used for executing queries, carrying the state needed to process them.
 * <p>
 * The query cursor should not be shared between threads, but can be reused for many query executions.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueryCursor extends External implements Iterable<QueryMatch> {

    Node node;
    Query query;

    @SuppressWarnings("unused")
    boolean executed = false;

    @SuppressWarnings("unused")
    QueryCursor(long pointer, Node node, Query query) {
        super(pointer);
        this.node = node;
        this.query = query;
    }

    /**
     * @deprecated Use {@link Node#walk(Query)} instead
     * @throws UnsupportedOperationException when called
     */
    @Deprecated(since = "1.5.0", forRemoval = true)
    public QueryCursor(@NotNull Node node, @NotNull Query query) {
        super();
        throw new UnsupportedOperationException(
                "This constructor should no longer be used"
        );
    }

    /**
     * Delete the query cursor, freeing all the memory that it used.
     */
    @Override
    public native void close();

    /**
     * Start running a given query on a given node.
     * Successive calls to this method are ignored.
     */
    public native void execute();

    /**
     * @return {@code true} if the query was executed, {@code false} otherwise
     * @since 1.5.0
     */
    public native boolean isExecuted();

    /**
     * Advance to the next match of the currently running query.
     *
     * @return A match if there is one, null otherwise
     * @throws IllegalStateException if {@link #execute()} was not called beforehand
     */
    public native QueryMatch nextMatch();

    /**
     * @return An iterator over the query cursor matches, starting from the first match
     */
    @Override
    public @NotNull Iterator<QueryMatch> iterator() {
        execute();
        return new Iterator<>() {

            private QueryMatch current = nextMatch();

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public QueryMatch next() {
                if (!hasNext()) throw new NoSuchElementException();
                QueryMatch match = current;
                current = nextMatch();
                return match;
            }
        };
    }

    @Override
    @Generated
    public String toString() {
        return String.format("QueryCursor(node: %s, query: %s)", node, query);
    }
}
