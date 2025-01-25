package org.bayl.vm.executor.operator.logical;

import lombok.EqualsAndHashCode;
import org.bayl.model.BytecodeToken;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.compile.jit.JITExecutorsWrapper;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.UnaryOpExecutor;
import org.bayl.vm.executor.maker.BooleanOpExecutor;

@EqualsAndHashCode(callSuper = true)
public class NotOpExecutor extends UnaryOpExecutor implements BooleanOpExecutor {

    public NotOpExecutor(SourcePosition pos, Executor operand) {
        super(pos, "not", operand);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylBoolean operand = getOperand().eval(virtualMachine).toBoolean(getOperand().getPosition());

        var isCanRunNative = isCanRunNative(operand);

        if (isCanRunNative instanceof BooleanNativeParseResult(boolean left1)) {
            var nativeResult = JITExecutorsWrapper.not(left1);
            return BaylBoolean.valueOf(nativeResult);
        }

        return operand.not();
    }

    protected BytecodeToken getTargetToken() {
        return BytecodeToken.NOT;
    }
}
