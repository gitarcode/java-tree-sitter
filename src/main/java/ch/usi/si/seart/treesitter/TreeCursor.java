package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Generated;
import lombok.experimental.FieldDefaults;

import java.util.function.Consumer;

/**
 * A tree cursor is a stateful object that allows you to walk a syntax tree with maximum efficiency.
 *
 * @since 1.0.0
 * @author Tommy MacWilliam
 * @author Ozren Dabić
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreeCursor extends External {

    int context0;
    int context1;

    long id;
    long tree;

    TreeCursor(Node node) {
        super(malloc(node));
    }

    private static native long malloc(Node node);

    /**
     * Delete the tree cursor, freeing all the memory that it used.
     */
    @Override
    public native void close();

    /**
     * @return The tree cursor's current node
     */
    public native Node getCurrentNode();

    /**
     * @return The field name of the tree cursor's current node,
     * {@code null} if the current node doesn't have a field
     */
    public native String getCurrentFieldName();

    /**
     * @return The tree cursor's current node
     */
    public native TreeCursorNode getCurrentTreeCursorNode();

    /**
     * Move the cursor to the first child of its current node.
     *
     * @return true if the cursor successfully moved,
     * and false if there were no children
     */
    public native boolean gotoFirstChild();

    /**
     * Move the cursor to the next sibling of its current node.
     *
     * @return true if the cursor successfully moved,
     * and false if there was no next sibling node
     */
    public native boolean gotoNextSibling();

    /**
     * Move the cursor to the parent of its current node.
     *
     * @return true if the cursor successfully moved,
     * and false if there was no parent node
     * (the cursor was already on the root node)
     */
    public native boolean gotoParent();

    /**
     * Iteratively traverse over the parse tree,
     * applying a callback to the nodes before they are visited.
     *
     * @param callback The callback consumer which will execute upon visiting a node
     */
    public void preorderTraversal(Consumer<Node> callback) {
        for (;;) {
            callback.accept(this.getCurrentNode());
            if (this.gotoFirstChild() || this.gotoNextSibling())
                continue;
            do {
                if (!this.gotoParent())
                    return;
            } while (!this.gotoNextSibling());
        }
    }

    @Override
    @Generated
    public String toString() {
        return String.format("TreeCursor(id: %d, tree: %d)", id, tree);
    }
}
