package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class LessThanOpNode extends RelationalOpNode {
    public LessThanOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "<", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return ZemBoolean.valueOf(compare(interpreter) < 0);
    }
}
