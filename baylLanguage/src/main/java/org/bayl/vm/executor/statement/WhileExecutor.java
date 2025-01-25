package org.bayl.vm.executor.statement;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.Environment;
import org.bayl.vm.executor.Executor;

@EqualsAndHashCode(callSuper = true)
public class WhileExecutor extends Executor {

    private final Executor testCondition;
    private final Executor loopBody;

    public WhileExecutor(SourcePosition pos, Executor testCondition, Executor loopBody) {
        super(pos);
        this.testCondition = testCondition;
        this.loopBody = loopBody;
    }

    public Executor getTestCondition() {
        return testCondition;
    }

    public Executor getLoopBody() {
        return loopBody;
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylObject ret = null;
        while (testCondition.eval(virtualMachine).toBoolean(testCondition.getPosition()).booleanValue()) {
            ret = loopBody.eval(virtualMachine);
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append("while ");
        sb.append(testCondition);
        sb.append(' ');
        sb.append(loopBody);
        sb.append(')');
        return sb.toString();
    }
}
