package org.bayl.vm.executor.expression;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.maker.ArithmeticOpExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class PowerOpExecutor extends BinaryOpExecutor implements ArithmeticOpExecutor {

    public PowerOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "^", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylNumber left = getLeft().eval(virtualMachine).toNumber(getLeft().getPosition());
        BaylNumber right = getRight().eval(virtualMachine).toNumber(getRight().getPosition());
        return left.power(right);
    }
}
