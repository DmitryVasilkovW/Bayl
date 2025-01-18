package org.bayl.vm.executor.statement;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.InvalidTypeException;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.expression.collection.LookupExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public class AssignExecutor extends BinaryOpExecutor {

    public AssignExecutor(SourcePosition pos, Executor varExecutor, Executor expression) {
        super(pos, "set!", varExecutor, expression);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        Executor left = getLeft();
        BaylObject value = getRight().eval(virtualMachine);
        if (left instanceof VariableExecutor) {
            String name = ((VariableExecutor) left).getName();
            virtualMachine.setVariable(name, value);
            return value;
        } else if (left instanceof LookupExecutor) {
            ((LookupExecutor) left).set(virtualMachine, value);
            return value;
        }
        throw new InvalidTypeException("Left hand of assignment must be a variable.", left.getPosition());
    }
}
