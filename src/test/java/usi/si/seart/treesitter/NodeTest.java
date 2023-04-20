package usi.si.seart.treesitter;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class NodeTest extends TestBase {

    private static final String source = "def foo(bar, baz):\n  print(bar)\n  print(baz)";
    private static Parser parser;
    private static Tree tree;
    private static Node root;

    @BeforeAll
    @SneakyThrows(UnsupportedEncodingException.class)
    static void beforeAll() {
        parser = new Parser(Language.PYTHON);
        tree = parser.parseString(source);
        root = tree.getRootNode();
    }

    @AfterAll
    static void afterAll() {
        tree.close();
        parser.close();
    }

    @Test
    void testGetChild() {
        Node function = root.getChild(0);
        Assertions.assertEquals(1, root.getChildCount());
        Assertions.assertEquals("module", root.getType());
        Assertions.assertEquals(0, root.getStartByte());
        Assertions.assertEquals(44, root.getEndByte());
        Assertions.assertEquals("function_definition", function.getType());
        Assertions.assertEquals(5, function.getChildCount());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(-1));
    }

    @Test
    void testGetChildByFieldName() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertEquals(identifier, function.getChildByFieldName("name"));
        Assertions.assertThrows(NullPointerException.class, () -> root.getChildByFieldName(null));
    }

    @Test
    void testGetDescendantForByteRange() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Node colon = function.getChild(3);
        Node body = function.getChild(4);
        Assertions.assertEquals(def, root.getDescendantForByteRange(def.getStartByte(), def.getEndByte()));
        Assertions.assertEquals(identifier, root.getDescendantForByteRange(identifier.getStartByte(), identifier.getEndByte()));
        Assertions.assertEquals(parameters, root.getDescendantForByteRange(parameters.getStartByte(), parameters.getEndByte()));
        Assertions.assertEquals(colon, root.getDescendantForByteRange(colon.getStartByte(), colon.getEndByte()));
        Assertions.assertEquals(body, root.getDescendantForByteRange(body.getStartByte(), body.getEndByte()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> root.getDescendantForByteRange(2, 0));
    }

    @Test
    void testGetFieldNameForChild() {
        Node function = root.getChild(0);
        Assertions.assertNull(function.getFieldNameForChild(0));                  // `def`
        Assertions.assertEquals("name", function.getFieldNameForChild(1));        // "name"
        Assertions.assertEquals("parameters", function.getFieldNameForChild(2));  // "parameters"
        Assertions.assertNull(function.getFieldNameForChild(3));                  // `:`
        Assertions.assertEquals("body", function.getFieldNameForChild(4));        // "body"
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> root.getFieldNameForChild(-1));
    }

    @Test
    void testGetFirstChildForByte() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Node colon = function.getChild(3);
        Node body = function.getChild(4);
        Assertions.assertEquals(def, function.getFirstChildForByte(def.getStartByte()));
        Assertions.assertEquals(identifier, function.getFirstChildForByte(identifier.getStartByte()));
        Assertions.assertEquals(parameters, function.getFirstChildForByte(parameters.getStartByte()));
        Assertions.assertEquals(colon, function.getFirstChildForByte(colon.getStartByte()));
        Assertions.assertEquals(body, function.getFirstChildForByte(body.getStartByte()));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> root.getFirstChildForByte(-1));
    }

    @Test
    void testGetFirstNamedChildForByte() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Node colon = function.getChild(3);
        Node body = function.getChild(4);
        Assertions.assertEquals(identifier, function.getFirstNamedChildForByte(def.getStartByte()));
        Assertions.assertEquals(identifier, function.getFirstNamedChildForByte(identifier.getStartByte()));
        Assertions.assertEquals(parameters, function.getFirstNamedChildForByte(parameters.getStartByte()));
        Assertions.assertEquals(body, function.getFirstNamedChildForByte(colon.getStartByte()));
        Assertions.assertEquals(body, function.getFirstNamedChildForByte(body.getStartByte()));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> root.getFirstNamedChildForByte(-1));
    }

    @Test
    void testGetParent() {
        Assertions.assertNull(root.getParent());
        Assertions.assertEquals(root, root.getChild(0).getParent());
    }

    @Test
    void testGetNextNamedSibling() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertNull(root.getNextNamedSibling());
        Assertions.assertEquals(identifier, def.getNextNamedSibling());
    }

    @Test
    void testGetNextSibling() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertNull(root.getNextSibling());
        Assertions.assertEquals(identifier, def.getNextSibling());
    }

    @Test
    void testGetPrevNamedSibling() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Assertions.assertNull(root.getPrevNamedSibling());
        Assertions.assertEquals(identifier, parameters.getPrevNamedSibling());
    }

    @Test
    void testGetPrevSibling() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertNull(root.getPrevSibling());
        Assertions.assertEquals(def, identifier.getPrevSibling());
    }

    @Test
    @SneakyThrows(UnsupportedEncodingException.class)
    void testHasError() {
        @Cleanup Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar.)");
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Assertions.assertTrue(root.hasError());
        Assertions.assertTrue(function.hasError());
        Assertions.assertFalse(def.hasError());
    }

    @Test
    @SneakyThrows(UnsupportedEncodingException.class)
    void testIsExtra() {
        @Cleanup Tree tree = parser.parseString("# this is just a comment");
        Node root = tree.getRootNode();
        Node comment = root.getChild(0);
        Assertions.assertFalse(root.isExtra());
        Assertions.assertTrue(comment.isExtra());
    }

    @Test
    @SneakyThrows(UnsupportedEncodingException.class)
    void testIsMissing() {
        @Cleanup Parser parser = new Parser(Language.JAVA);
        @Cleanup Tree tree = parser.parseString("class C { public static final int i = 6 }");
        Node root = tree.getRootNode();
        Assertions.assertFalse(root.isMissing());
        Assertions.assertFalse(root.getChild(0).isMissing());
        Assertions.assertFalse(root.getChild(0).getChild(2).isMissing());
        Assertions.assertFalse(root.getChild(0).getChild(2).getChild(1).isMissing());
        Assertions.assertFalse(root.getChild(0).getChild(2).getChild(1).getChild(2).isMissing());
        Assertions.assertTrue(root.getChild(0).getChild(2).getChild(1).getChild(3).isMissing());
    }

    @Test
    void testIsNamed() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertFalse(def.isNamed());
        Assertions.assertTrue(identifier.isNamed());
    }

    @Test
    void testIsNull() {
        Assertions.assertFalse(root.isNull());
        Assertions.assertTrue(new Node().isNull());
    }
}
