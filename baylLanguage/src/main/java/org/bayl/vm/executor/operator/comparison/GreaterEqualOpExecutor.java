package org.bayl.vm.executor.operator.comparison;

import lombok.EqualsAndHashCode;
import org.bayl.model.BytecodeToken;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.compile.jit.JITExecutorsWrapper;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.RelationalOpExecutor;

@EqualsAndHashCode(callSuper = true)
public class GreaterEqualOpExecutor extends RelationalOpExecutor {

    public GreaterEqualOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, ">=", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        var baylObjectPair = getBaylObjectPair(virtualMachine);
        var left = baylObjectPair.left();
        var right = baylObjectPair.right();
        checkTypes(left, right);

        var isCanRunNative = isCanRunNative(left, right);

        if (isCanRunNative instanceof  DoublePairNativeParseResult(double left1, double right1)) {
            var nativeResult = JITExecutorsWrapper.greaterOrEqual(left1, right1);
            return BaylBoolean.valueOf(nativeResult);
        }

        return BaylBoolean.valueOf(compare(virtualMachine) >= 0);
    }

    @Override
    protected BytecodeToken getTargetToken() {
        return BytecodeToken.GREATER_EQUAL;
    }
}
