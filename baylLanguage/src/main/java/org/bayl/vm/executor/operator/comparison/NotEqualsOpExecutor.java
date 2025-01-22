package org.bayl.vm.executor.operator.comparison;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.RelationalOpExecutor;

@EqualsAndHashCode(callSuper = true)
public class NotEqualsOpExecutor extends RelationalOpExecutor {

    public NotEqualsOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "!=", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return evalEquals(virtualMachine).not();
    }
}
