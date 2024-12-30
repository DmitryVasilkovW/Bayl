package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class AndOpNode extends BinaryOpNode implements IBooleanOpNode {
    public AndOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "and", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemBoolean left = getLeft().eval(interpreter).toBoolean(getLeft().getPosition());
        if (!left.booleanValue()) {
            return left;
        }
        ZemBoolean right = getRight().eval(interpreter).toBoolean(getRight().getPosition());
        return left.and(right);
    }
}
