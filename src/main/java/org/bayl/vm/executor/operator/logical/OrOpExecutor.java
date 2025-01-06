package org.bayl.vm.executor.operator.logical;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.maker.BooleanOpExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class OrOpExecutor extends BinaryOpExecutor implements BooleanOpExecutor {

    public OrOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "or", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylBoolean left = getLeft().eval(virtualMachine).toBoolean(getLeft().getPosition());
        if (left.booleanValue()) {
            return left;
        }
        BaylBoolean right = getRight().eval(virtualMachine).toBoolean(getRight().getPosition());
        return left.or(right);
    }
}
