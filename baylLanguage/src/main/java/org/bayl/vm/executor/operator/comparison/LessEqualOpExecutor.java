package org.bayl.vm.executor.operator.comparison;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.RelationalOpExecutor;

@EqualsAndHashCode(callSuper = true)
public class LessEqualOpExecutor extends RelationalOpExecutor {

    public LessEqualOpExecutor(SourcePosition pos, Executor left, Executor right) {
        super(pos, "<=", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return BaylBoolean.valueOf(compare(virtualMachine) <= 0);
    }
}
