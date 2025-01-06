package org.bayl.vm.executor.operator.comparison;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.RelationalOpExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class NotEqualsOpExecutor extends RelationalOpExecutor {

    public NotEqualsOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "!=", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return equals(virtualMachine).not();
    }
}
