package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemBoolean;
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
}
