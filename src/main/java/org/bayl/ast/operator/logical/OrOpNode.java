package org.bayl.ast.operator.logical;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.IBooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.runtime.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class OrOpNode extends BinaryOpNode implements IBooleanOpNode {
    public OrOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "or", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemBoolean left = getLeft().eval(interpreter).toBoolean(getLeft().getPosition());
        if (left.booleanValue()) {
            return left;
        }
        ZemBoolean right = getRight().eval(interpreter).toBoolean(getRight().getPosition());
        return left.or(right);
    }
}
