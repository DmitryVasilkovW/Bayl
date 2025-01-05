package org.bayl.ast.expression;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.runtime.BaylObject;

public class TrueNode extends Node {
    public TrueNode(SourcePosition pos) {
        super(pos);
    }

    @Override
    public BaylObject eval(Interpreter interpreter) {
        return BaylBoolean.TRUE;
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
