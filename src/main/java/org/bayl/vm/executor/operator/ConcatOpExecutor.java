package org.bayl.vm.executor.operator;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class ConcatOpExecutor extends BinaryOpExecutor {

    public ConcatOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "~", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylString left = getLeft().eval(virtualMachine).toZString();
        BaylString right = getRight().eval(virtualMachine).toZString();
        return left.concat(right);
    }
}
