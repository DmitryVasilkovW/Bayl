package org.bayl.vm.executor.operator.arithmetic;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.UnaryOpExecutor;
import org.bayl.vm.executor.maker.ArithmeticOpExecutor;

@EqualsAndHashCode(callSuper = true)
public class NegateOpExecutor extends UnaryOpExecutor implements ArithmeticOpExecutor {

    public NegateOpExecutor(SourcePosition pos, Executor value) {
        super(pos, "-", value);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylNumber operand = getOperand().eval(virtualMachine).toNumber(getOperand().getPosition());
        return operand.negate();
    }
}
