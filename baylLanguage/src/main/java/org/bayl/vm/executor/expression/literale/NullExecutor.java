package org.bayl.vm.executor.expression.literale;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylNull;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;

public class NullExecutor extends Executor {

    private static final String VALUE = "null";

    public NullExecutor(SourcePosition position) {
        super(position);
    }

    @Override
    public String toString() {
        return VALUE;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return new BaylNull();
    }
}
