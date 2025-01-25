package org.bayl.vm.executor.classes;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.classes.UserClass;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;

public class ClassExecutor extends Executor {

    private final BlockExecutor body;

    public ClassExecutor(SourcePosition position, BlockExecutor body) {
        super(position);
        this.body = body;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("class");
        sb.append(body.toString());

        return sb.toString();
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return new UserClass(body);
    }
}
