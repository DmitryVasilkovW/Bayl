package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemBoolean;
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
}
