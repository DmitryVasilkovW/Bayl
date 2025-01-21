package org.bayl.syntax;

import org.bayl.ast.control.RootNode;
import org.bayl.ast.expression.literale.FalseNode;
import org.bayl.ast.operator.arithmetic.ModOpNode;
import org.bayl.ast.operator.arithmetic.PowerOpNode;
import org.bayl.ast.expression.literale.TrueNode;
import org.bayl.ast.expression.literale.NumberNode;
import org.bayl.ast.expression.literale.StringNode;
import org.bayl.ast.operator.arithmetic.AddOpNode;
import org.bayl.ast.operator.arithmetic.DivideOpNode;
import org.bayl.ast.operator.arithmetic.MultiplyOpNode;
import org.bayl.ast.operator.arithmetic.SubtractOpNode;
import org.bayl.ast.statement.AssignNode;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {

    @Test
    public void testAssignment() {
        try {
            Lexer lexer = new Lexer(new StringReader("n = 0;"));
            Parser parser = new Parser(lexer);
            RootNode node = parser.program();
            AssignNode assignment = (AssignNode) node.get(0);
            assertSame(AssignNode.class, assignment.getClass());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void assertType(String snippet, Class<?> type) {
        try {
            Lexer lexer = new Lexer(new StringReader(snippet));
            Parser parser = new Parser(lexer);
            RootNode node = parser.program();
            AssignNode assignment = (AssignNode) node.get(0);
            assertSame(assignment.getRight().getClass(), type);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testConstants() {
        assertType("n = 3;", NumberNode.class);
        assertType("n = 'hello';", StringNode.class);
        assertType("n = true;", TrueNode.class);
        assertType("n = false;", FalseNode.class);
    }

    @Test
    public void testArithmeticOperators() {
        assertType("n = 1 + 1;", AddOpNode.class);
        assertType("n = 3 - 2;", SubtractOpNode.class);
        assertType("n = 2 * 2;", MultiplyOpNode.class);
        assertType("n = 4 / 2;", DivideOpNode.class);
        assertType("n = 4 % 3;", ModOpNode.class);
        assertType("n = 2 ^ 2;", PowerOpNode.class);
    }

    private void assertSyntax(String snippet) {
        try {
            Lexer lexer = new Lexer(new StringReader(snippet));
            Parser parser = new Parser(lexer);
            RootNode node = parser.program();
            assertNotNull(node);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void assertSExpr(String script, String sexpr) {
        try {
            Lexer lexer = new Lexer(new StringReader(script));
            Parser parser = new Parser(lexer);
            RootNode node = parser.program();
            assertEquals(sexpr, node.toString());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOperatorChain() {
        assertSyntax("n = 2 + 2 - 2 + 2;");
        assertSyntax("n = 2 * 2 / 2 * 2;");
        assertSyntax("n = 2 ^ 2 ^ 2;");
        assertSyntax("n = true && true && false || false || true;");
    }

    @Test
    public void testAssociativity() {
        assertSExpr("n = 1 + 2 + 3;", "(set! n (+ (+ 1 2) 3))");
        assertSExpr("n = 1 - 2 - 3;", "(set! n (- (- 1 2) 3))");
        assertSExpr("n = 1 * 2 * 3;", "(set! n (* (* 1 2) 3))");
        assertSExpr("n = 1 / 2 / 3;", "(set! n (/ (/ 1 2) 3))");
        assertSExpr("n = 4 ^ 3 ^ 2;", "(set! n (^ 4 (^ 3 2)))");
    }

    @Test
    public void testOperatorPrecedence() {
        assertSExpr("n = 1 - -2;", "(set! n (- 1 (- 2)))");
        assertSExpr("n = 2 + 2 * 3;", "(set! n (+ 2 (* 2 3)))");
        assertSExpr("n = 2 - 4 / 2;", "(set! n (- 2 (/ 4 2)))");
        assertSExpr("n = 2 + 1 * 2 ^ 2;", "(set! n (+ 2 (* 1 (^ 2 2))))");
        assertSExpr("n = 2 ^ -2;", "(set! n (^ 2 (- 2)))");
        assertSExpr("n = -2 ^ 2;", "(set! n (^ (- 2) 2))");
        assertSExpr("n = a || b && c;", "(set! n (or a (and b c)))");
        assertSExpr("n = a && b || c;", "(set! n (or (and a b) c))");
        assertSExpr("n = a && b || c && d;", "(set! n (or (and a b) (and c d)))");
        assertSExpr("n = a && b || c && d || e;", "(set! n (or (and a b) (or (and c d) e)))");
        assertSExpr("n = !a && b;", "(set! n (and (not a) b))");
        assertSExpr("n = a && !b;", "(set! n (and a (not b)))");

        assertSExpr("n = (2 + 2) * 3;", "(set! n (* (+ 2 2) 3))");
        assertSExpr("n = !(a && b);", "(set! n (not (and a b)))");

        assertSExpr("n = 1 + 1 <= 2 || 3 * 2 > 5 && 5 * 1 > 4;",
                    "(set! n (or (<= (+ 1 1) 2) (and (> (* 3 2) 5) (> (* 5 1) 4))))");
    }

    @Test
    public void testControlStructures() {
        assertSExpr("if (cond) { then(); } else { somethingElse(); }", "(if cond ((then)) ((somethingElse)))");
        assertSExpr("while (cond) { body(); }", "(while cond ((body)))");
        assertSExpr("foreach (on_var as element) { process(element); }",
                    "(foreach on_var element ((process element)))");
        assertSExpr("foreach (on_var as key : value) { process(key, value); }",
                    "(foreach on_var (key value) ((process key value)))");
    }

    @Test
    public void testFunction() {
        assertSExpr("add = function(a, b) { return a + b; };", "(set! add (function (a b) ((return (+ a b)))))");
    }

    @Test
    public void testFunctionCall() {
        assertSExpr(
                "f = function(x) { return function(y) { return function(z) { return x + y + z; }; }; }; r = f(a)(b)(c);",
                "(set! f (function (x) ((return (function (y) ((return (function (z) ((return (+ (+ x y) z)))))))))))(set! r (((f a) b) c))");
        assertSExpr("function(x) { return x; }(42);", "((function (x) ((return x))) 42)");
        assertSExpr("y = function(x) { return x; }(42);", "(set! y ((function (x) ((return x))) 42))");
        assertSExpr("arr = [function(x) { return x; }]; arr[0]();",
                    "(set! arr '((function (x) ((return x))) ))((lookup arr 0))");
        assertSExpr("obj = { 'f' : function(x) { return x; } }; y = obj['f']();",
                    "(set! obj (dict (\"f\" (function (x) ((return x))))))(set! y ((lookup obj \"f\")))");
    }
}
