package org.bayl.vm.executor.operator.arithmetic;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.compile.jit.JITExecutorsWrapper;
import org.bayl.runtime.object.value.BaylNumber;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.maker.ArithmeticOpExecutor;

@EqualsAndHashCode(callSuper = true)
public class MultiplyOpExecutor extends BinaryOpExecutor implements ArithmeticOpExecutor {

    public MultiplyOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "*", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylNumber left = getLeft().eval(virtualMachine).toNumber(getLeft().getPosition());
        BaylNumber right = getRight().eval(virtualMachine).toNumber(getRight().getPosition());

        var isCanRunNative = isCanRunNative(left, right);
        if (isCanRunNative instanceof  DoublePairNativeParseResult(double left1, double right1)) {
            var nativeResult = JITExecutorsWrapper.multiply(left1, right1);
            return new BaylNumber(nativeResult);
        }

        return left.multiply(right);
    }
}
