package org.bayl.ast.expression;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.runtime.BaylObject;

public class FalseNode extends Node {
    public FalseNode(SourcePosition pos) {
        super(pos);
    }

    @Override
    public BaylObject eval(Interpreter interpreter) {
        return BaylBoolean.FALSE;
    }

    @Override
    public String toString() {
        return "false";
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("PUSH false");
    }
}
