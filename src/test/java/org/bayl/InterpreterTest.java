package org.bayl;

import java.io.IOException;

import org.bayl.runtime.object.BaylBoolean;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;

import org.junit.*;
import static org.junit.Assert.*;

public class InterpreterTest {
    private Interpreter interpreter = new Interpreter();

    /**
     * Helper method to test the result of an expression
     *
     * @param script Script to evaluate
     * @param expected The expected result
     */
    private void assertResult(String script, BaylObject expected) {
        try {
            BaylObject actual = interpreter.eval(script);
            assertEquals(expected, actual);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testArithmetic() {
        assertResult("n = 2 + 3;" , new BaylNumber("5"));
        assertResult("x = 3 - 2;", new BaylNumber("1"));
        assertResult("x = 2 - 3;", new BaylNumber("-1"));
        assertResult("x = 2 * 3;", new BaylNumber("6"));
        assertResult("x = 2 ^ 3;", new BaylNumber("8"));
        assertResult("x = 3 ^ 2;", new BaylNumber("9"));
        assertResult("x = 4 / 2;", new BaylNumber("2"));
        assertResult("x = 3 % 2;", new BaylNumber("1"));
        assertResult("x = 2 % 2;", new BaylNumber("0"));
        assertResult("x = 1 % 2;", new BaylNumber("1"));
        assertResult("x = 4 % 2;", new BaylNumber("0"));
        assertResult("x = 5 % 2;", new BaylNumber("1"));
        assertResult("x = -2;", new BaylNumber("-2"));
        assertResult("x = 1 - -1;", new BaylNumber("2"));
        // Test decimal operations
        assertResult("x = 0.1 + 0.2;", new BaylNumber("0.3"));
        assertResult("x = 0.1 - 0.2;", new BaylNumber("-0.1"));
        assertResult("x = 1 / 2;", new BaylNumber("0.5"));
        assertResult("x = 2 * 1.5;", new BaylNumber("3"));
        assertResult("x = 3 % 1.5;", new BaylNumber("0"));
        // Test operator precedence
        assertResult("x = 2 + 3 * 4;", new BaylNumber("14"));
        assertResult("x = (2 + 3) * 4;", new BaylNumber("20"));
        assertResult("x = 2 * 3 ^ 2;", new BaylNumber("18"));
    }

    @Test
    public void testNumbers() {
        // Hex digits
        assertResult("x = 0xA;", new BaylNumber("10"));
        assertResult("x = 0xB;", new BaylNumber("11"));
        assertResult("x = 0xC;", new BaylNumber("12"));
        assertResult("x = 0xD;", new BaylNumber("13"));
        assertResult("x = 0xE;", new BaylNumber("14"));
        assertResult("x = 0xF;", new BaylNumber("15"));
        // Hex
        assertResult("x = 0x3BE;", new BaylNumber("958"));
        // Octal
        assertResult("x = 0o52;", new BaylNumber("42"));
        // Binary
        assertResult("x = 0b101;", new BaylNumber("5"));
    }

    @Test
    public void testBooleanLogic() {
        assertResult("x = true && true;", BaylBoolean.TRUE);
        assertResult("x = false && true;", BaylBoolean.FALSE);
        assertResult("x = true && false;", BaylBoolean.FALSE);
        assertResult("x = false && false;", BaylBoolean.FALSE);
        assertResult("x = true || true;", BaylBoolean.TRUE);
        assertResult("x = false || true;", BaylBoolean.TRUE);
        assertResult("x = true || false;", BaylBoolean.TRUE);
        assertResult("x = false || false;", BaylBoolean.FALSE);
        assertResult("x = !true;", BaylBoolean.FALSE);
        assertResult("x = !false;", BaylBoolean.TRUE);
        // Test operator precedence
        assertResult("x = false || true && false;", BaylBoolean.FALSE);
        assertResult("x = false || true && !false;", BaylBoolean.TRUE);
    }

    @Test
    public void testRelationOps() {
        assertResult("x = 2 < 3;", BaylBoolean.TRUE);
        assertResult("x = 3 < 2;", BaylBoolean.FALSE);
        assertResult("x = 2 <= 3;", BaylBoolean.TRUE);
        assertResult("x = 3 <= 3;", BaylBoolean.TRUE);
        assertResult("x = 4 <= 3;", BaylBoolean.FALSE);
        assertResult("x = 2 == 2;", BaylBoolean.TRUE);
        assertResult("x = 2 == 3;", BaylBoolean.FALSE);
        assertResult("x = 2 == 1 + 1;", BaylBoolean.TRUE);
        assertResult("x = 2 != 2;", BaylBoolean.FALSE);
        assertResult("x = 2 != 3;", BaylBoolean.TRUE);
        assertResult("x = 2 > 3;", BaylBoolean.FALSE);
        assertResult("x = 3 > 2;", BaylBoolean.TRUE);
        assertResult("x = 2 >= 3;", BaylBoolean.FALSE);
        assertResult("x = 3 >= 3;", BaylBoolean.TRUE);
        assertResult("x = 4 >= 3;", BaylBoolean.TRUE);
    }

    @Test
    public void testConcat() {
        assertResult("x = 'hello' ~ ' world!';", new BaylString("hello world!"));
    }

    @Test
    public void testIf() {
        assertResult("if (true) { x = 'then'; }", new BaylString("then"));
        assertResult("if (false) { x = 'then'; } else { x = 'else'; }", new BaylString("else"));
        assertResult("if (false) { x = 'then'; } else if (true) { x = 'elseif'; } else { x = 'else'; }", new BaylString("elseif"));
    }

    @Test
    public void testWhile() {
        assertResult("i = 0; while (i < 9) { i = i + 1; } x = i;", new BaylNumber("9"));
    }

    @Test
    public void testForeach() {
        assertResult("array = [1, 2, 3]; t = 0; foreach (array as element) { t = t + element; } x = t;", new BaylNumber("6"));
        assertResult("dict = {'apples':1, 'oranges':3}; t = 0; foreach (dict as k : v) { t = t + v; } x = t;", new BaylNumber("4"));
    }

    @Test
    public void testFunction() {
        assertResult("add = function(a, b) { return a + b; }; x = add(2, 3);", new BaylNumber("5"));
        assertResult("sum = function(array) { t = 0; foreach (array as element) { t = t + element; } return t; }; t = sum([1, 2, 3]);", new BaylNumber("6"));
        // Test nested calls
        assertResult("x = add(2, add(2, 3));", new BaylNumber("7"));
        // Test return
        assertResult("test = function() { i = 0; while (i < 9) { i = i + 1; if (i == 5) { return i; } } return i; }; x = test();", new BaylNumber("5"));
    }

    @Test
    public void testFunctionCall() {
        assertResult("f = function() { return function() { return function() { return 'hello world'; }; }; }; x = f()()();", new BaylString("hello world"));
        assertResult("function(msg) { return msg; }('hello world');", new BaylString("hello world"));
        assertResult("x = function(msg) { return msg; }('hello world');", new BaylString("hello world"));
        assertResult("obj = { 'greet' : function() { return 'hello world'; } }; msg = obj['greet']();", new BaylString("hello world"));
    }
}
