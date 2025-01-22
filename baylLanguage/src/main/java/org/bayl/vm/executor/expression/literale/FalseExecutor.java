package org.bayl.vm.executor.expression.literale;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylBoolean;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;

@EqualsAndHashCode(callSuper = true)
public class FalseExecutor extends Executor {
    public FalseExecutor(SourcePosition pos) {
        super(pos);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return BaylBoolean.FALSE;
    }

    @Override
    public String toString() {
        return "false";
    }
}
