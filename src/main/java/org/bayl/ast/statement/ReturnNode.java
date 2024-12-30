package org.bayl.ast.statement;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.runtime.exception.ReturnException;
import org.bayl.runtime.ZemObject;

public class ReturnNode extends Node {
    private Node expression;

    public ReturnNode(SourcePosition pos, Node expression) {
        super(pos);
        this.expression = expression;
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        throw new ReturnException(expression.eval(interpreter));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(return ");
        sb.append(expression);
        sb.append(')');
        return sb.toString();
    }
}
