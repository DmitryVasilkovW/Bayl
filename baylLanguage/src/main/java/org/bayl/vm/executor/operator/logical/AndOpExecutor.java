package org.bayl.vm.executor.operator.logical;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.maker.BooleanOpExecutor;

@EqualsAndHashCode(callSuper = true)
public class AndOpExecutor extends BinaryOpExecutor implements BooleanOpExecutor {

    public AndOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "and", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylBoolean left = getLeft().eval(virtualMachine).toBoolean(getLeft().getPosition());
        if (!left.booleanValue()) {
            return left;
        }
        BaylBoolean right = getRight().eval(virtualMachine).toBoolean(getRight().getPosition());
        return left.and(right);
    }
}
