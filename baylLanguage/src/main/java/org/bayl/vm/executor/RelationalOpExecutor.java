package org.bayl.vm.executor;

import lombok.EqualsAndHashCode;
import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidOperatorException;
import org.bayl.runtime.exception.TypeMismatchException;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public abstract class RelationalOpExecutor extends BinaryOpExecutor {

    public RelationalOpExecutor(SourcePosition pos, String operator, Executor left, Executor right) {
        super(pos, operator, left, right);
    }

    protected void checkTypes(BaylObject left, BaylObject right) {
        if (!left.getClass().equals(right.getClass())) {
            throw new TypeMismatchException(getPosition(), left.getClass(), right.getClass());
        }
    }

    protected int compare(VirtualMachineImpl interpreter) {
        BaylObject left = getLeft().eval(interpreter);
        BaylObject right = getRight().eval(interpreter);
        checkTypes(left, right);
        try {
            return left.compareTo(right);
        } catch (UnsupportedOperationException e) {
            throw new InvalidOperatorException(getPosition());
        }
    }

    protected BaylBoolean evalEquals(VirtualMachineImpl interpreter) {
        BaylObject left = getLeft().eval(interpreter);
        BaylObject right = getRight().eval(interpreter);
        checkTypes(left, right);
        return BaylBoolean.valueOf(left.equals(right));
    }
}
