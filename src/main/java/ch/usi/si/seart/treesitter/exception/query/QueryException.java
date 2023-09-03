package ch.usi.si.seart.treesitter.exception.query;

import ch.usi.si.seart.treesitter.exception.TreeSitterException;
import lombok.experimental.StandardException;

/**
 * The base exception type for all exceptions related to tree-sitter queries.
 *
 * @author Ozren Dabić
 */
@StandardException
public abstract class QueryException extends TreeSitterException {
}
