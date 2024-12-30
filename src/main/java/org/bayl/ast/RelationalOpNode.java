package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.runtime.exception.InvalidOperatorException;
import org.bayl.SourcePosition;
import org.bayl.runtime.exception.TypeMismatchException;
import org.bayl.runtime.object.ZemBoolean;
import org.bayl.runtime.ZemObject;

public abstract class RelationalOpNode extends BinaryOpNode {
    public RelationalOpNode(SourcePosition pos, String operator, Node left, Node right) {
        super(pos, operator, left, right);
    }

    protected void checkTypes(ZemObject left, ZemObject right) {
        if (!left.getClass().equals(right.getClass())) {
            throw new TypeMismatchException(getPosition(), left.getClass(), right.getClass());
        }
    }

    protected int compare(Interpreter interpreter) {
        ZemObject left = getLeft().eval(interpreter);
        ZemObject right = getRight().eval(interpreter);
        checkTypes(left, right);
        try {
            return left.compareTo(right);
        } catch (UnsupportedOperationException e) {
            throw new InvalidOperatorException(getPosition());
        }
    }

    protected ZemBoolean equals(Interpreter interpreter) {
        ZemObject left = getLeft().eval(interpreter);
        ZemObject right = getRight().eval(interpreter);
        checkTypes(left, right);
        return ZemBoolean.valueOf(left.equals(right));
    }
}
