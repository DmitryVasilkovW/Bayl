package org.bayl.vm.executor;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.impl.VirtualMachineImpl;

public abstract class Executor {

    private final SourcePosition position;

    public Executor(SourcePosition position) {
        this.position = position;
    }

    public SourcePosition getPosition() {
        return position;
    }

    public abstract BaylObject eval(VirtualMachineImpl virtualMachine);
}
