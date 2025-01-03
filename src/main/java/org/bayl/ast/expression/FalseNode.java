package org.bayl.ast.expression;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class FalseNode extends Node {
    public FalseNode(SourcePosition pos) {
        super(pos);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return ZemBoolean.FALSE;
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
