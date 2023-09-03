package ch.usi.si.seart.treesitter.exception.query;

import lombok.experimental.StandardException;

/**
 * Thrown when a specified node type does not exist in the language grammar.
 */
@SuppressWarnings("unused")
@StandardException
public class QueryNodeTypeException extends QueryException {

    public QueryNodeTypeException(int offset) {
        super("Bad node name at offset " + offset);
    }
}
