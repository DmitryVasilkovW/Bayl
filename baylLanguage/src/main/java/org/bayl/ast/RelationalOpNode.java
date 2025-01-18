package org.bayl.ast;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidOperatorException;
import org.bayl.runtime.exception.TypeMismatchException;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.Environment;

public abstract class RelationalOpNode extends BinaryOpNode {

    public RelationalOpNode(SourcePosition pos, String operator, Node left, Node right) {
        super(pos, operator, left, right);
    }

    protected void checkTypes(BaylObject left, BaylObject right) {
        if (!left.getClass().equals(right.getClass())) {
            throw new TypeMismatchException(getPosition(), left.getClass(), right.getClass());
        }
    }

    protected int compare(Environment interpreter) {
        BaylObject left = getLeft().eval(interpreter);
        BaylObject right = getRight().eval(interpreter);
        checkTypes(left, right);
        try {
            return left.compareTo(right);
        } catch (UnsupportedOperationException e) {
            throw new InvalidOperatorException(getPosition());
        }
    }

    protected BaylBoolean equals(Environment interpreter) {
        BaylObject left = getLeft().eval(interpreter);
        BaylObject right = getRight().eval(interpreter);
        checkTypes(left, right);
        return BaylBoolean.valueOf(left.equals(right));
    }
}
