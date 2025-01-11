package org.bayl.vm.executor.operator.comparison;

import lombok.EqualsAndHashCode;
import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.RelationalOpExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public class EqualsOpExecutor extends RelationalOpExecutor {

    public EqualsOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "==", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return evalEquals(virtualMachine);
    }
}
