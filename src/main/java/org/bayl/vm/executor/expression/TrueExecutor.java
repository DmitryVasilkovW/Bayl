package org.bayl.vm.executor.expression;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class TrueExecutor extends Executor {

    public TrueExecutor(SourcePosition pos) {
        super(pos);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return BaylBoolean.TRUE;
    }

    @Override
    public String toString() {
        return "true";
    }
}
