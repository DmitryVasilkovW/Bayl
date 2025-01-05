package org.bayl.ast.expression;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class TrueNode extends Node {
    public TrueNode(SourcePosition pos) {
        super(pos);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return ZemBoolean.TRUE;
    }

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("PUSH true");
    }
}
