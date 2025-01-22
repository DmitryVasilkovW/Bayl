package org.bayl.vm.executor.expression.variable;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;

@EqualsAndHashCode(callSuper = true)
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
    public BaylObject eval(Environment virtualMachine) {
        return virtualMachine.getVariable(name, getPosition());
    }
}
