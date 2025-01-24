package org.bayl.vm.executor.operator.logical;

import lombok.EqualsAndHashCode;
import org.bayl.model.BytecodeToken;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.compile.jit.JITExecutorsWrapper;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;

@EqualsAndHashCode(callSuper = true)
public class AndOpExecutor extends BinaryOpExecutor {

    public AndOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "and", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylBoolean left = getLeft().eval(virtualMachine).toBoolean(getLeft().getPosition());
        BaylBoolean right = getRight().eval(virtualMachine).toBoolean(getRight().getPosition());

        if (!left.booleanValue()) {
            return left;
        }

        var isCanRunNative = isCanRunNative(left, right);
        if (isCanRunNative instanceof BooleanPairNativeParseResult(boolean left1, boolean right1)) {
            var nativeResult = JITExecutorsWrapper.and(left1, right1);
            return BaylBoolean.valueOf(nativeResult);
        }

        return left.and(right);
    }

    protected BytecodeToken getTargetToken() {
        return BytecodeToken.AND;
    }
}
