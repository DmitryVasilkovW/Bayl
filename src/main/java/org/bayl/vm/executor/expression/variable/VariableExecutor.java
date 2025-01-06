package org.bayl.vm.executor.expression.variable;

import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

public class VariableExecutor extends Executor {

    private final String name;

    public VariableExecutor(SourcePosition pos, String variableName) {
        super(pos);
        name = variableName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return virtualMachine.getVariable(name, getPosition());
    }
}
