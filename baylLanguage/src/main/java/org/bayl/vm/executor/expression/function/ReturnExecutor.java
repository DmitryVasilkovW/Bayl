package org.bayl.vm.executor.expression.function;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.exception.ReturnException;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public class ReturnExecutor extends Executor {

    private final Executor expression;

    public ReturnExecutor(SourcePosition pos, Executor expression) {
        super(pos);
        this.expression = expression;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        throw new ReturnException(expression.eval(virtualMachine));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(return ");
        sb.append(expression);
        sb.append(')');
        return sb.toString();
    }
}
