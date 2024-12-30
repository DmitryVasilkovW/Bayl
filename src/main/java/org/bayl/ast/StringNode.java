package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemObject;
import org.bayl.runtime.ZemString;

public class StringNode extends Node {
    private ZemString literal;

    public StringNode(SourcePosition pos, String literal) {
        super(pos);
        this.literal = new ZemString(literal);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return literal;
    }

    @Override
    public String toString() {
        return '"' + literal.toString() + '"';
    }
}
