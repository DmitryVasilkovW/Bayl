package org.bayl.vm.executor.operator.logical;

import lombok.EqualsAndHashCode;
import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.UnaryOpExecutor;
import org.bayl.vm.executor.maker.BooleanOpExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public class NotOpExecutor extends UnaryOpExecutor implements BooleanOpExecutor {

    public NotOpExecutor(SourcePosition pos, Executor operand) {
        super(pos, "not", operand);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylBoolean operand = getOperand().eval(virtualMachine).toBoolean(getOperand().getPosition());
        return operand.not();
    }
}
