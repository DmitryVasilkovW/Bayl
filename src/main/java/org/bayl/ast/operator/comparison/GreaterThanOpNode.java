package org.bayl.ast.operator.comparison;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.runtime.object.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class GreaterThanOpNode extends RelationalOpNode {
    public GreaterThanOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, ">", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return ZemBoolean.valueOf(compare(interpreter) > 0);
    }
}
