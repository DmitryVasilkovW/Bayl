package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemObject;

public class EqualsOpNode extends RelationalOpNode {
    public EqualsOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "==", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return equals(interpreter);
    }
}
