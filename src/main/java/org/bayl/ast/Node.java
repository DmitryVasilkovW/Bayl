package org.bayl.ast;

import org.bayl.SourcePosition;
import org.bayl.Interpreter;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.ZemObject;

public abstract class Node {
    private SourcePosition position;

    public Node(SourcePosition position) {
        this.position = position;
    }

    public SourcePosition getPosition() {
        return position;
    }

    abstract public ZemObject eval(Interpreter interpreter);

    public abstract void generateCode(Bytecode bytecode);
}
