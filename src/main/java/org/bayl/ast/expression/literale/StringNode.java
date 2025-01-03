package org.bayl.ast.expression.literale;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.ZemObject;
import org.bayl.runtime.object.ZemString;

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

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("PUSH \"" + literal.toString() + "\"");
    }
}
