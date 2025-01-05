package org.bayl.ast.expression.literale;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;

public class StringNode extends Node {
    private BaylString literal;

    public StringNode(SourcePosition pos, String literal) {
        super(pos);
        this.literal = new BaylString(literal);
    }

    @Override
    public BaylObject eval(Interpreter interpreter) {
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
