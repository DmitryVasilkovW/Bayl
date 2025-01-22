package org.bayl.vm.executor.operator.string;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylString;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.BinaryOpExecutor;
import org.bayl.vm.executor.Executor;

@EqualsAndHashCode(callSuper = true)
public class ConcatOpExecutor extends BinaryOpExecutor {

    public ConcatOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "~", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylString left = getLeft().eval(virtualMachine).toBaylString();
        BaylString right = getRight().eval(virtualMachine).toBaylString();
        return left.concat(right);
    }
}
