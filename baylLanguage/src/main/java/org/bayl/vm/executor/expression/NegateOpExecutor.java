package org.bayl.vm.executor.expression;

import lombok.EqualsAndHashCode;
import org.bayl.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.UnaryOpExecutor;
import org.bayl.vm.executor.maker.ArithmeticOpExecutor;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public class NegateOpExecutor extends UnaryOpExecutor implements ArithmeticOpExecutor {

    public NegateOpExecutor(SourcePosition pos, Executor value) {
        super(pos, "-", value);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylNumber operand = getOperand().eval(virtualMachine).toNumber(getOperand().getPosition());
        return operand.negate();
    }
}
