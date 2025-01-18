package org.bayl.vm.executor.statement;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.impl.VirtualMachineImpl;

@EqualsAndHashCode(callSuper = true)
public class IfExecutor extends Executor {

    private final Executor testCondition;
    private final Executor thenBlock;
    private final Executor elseBlock;

    public IfExecutor(SourcePosition pos, Executor testCondition, Executor thenBlock, Executor elseBlock) {
        super(pos);
        this.testCondition = testCondition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public Executor getTestCondition() {
        return testCondition;
    }

    public Executor getThenBlock() {
        return thenBlock;
    }

    public Executor getElseBlock() {
        return elseBlock;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        boolean test = testCondition.eval(virtualMachine).toBoolean(testCondition.getPosition()).booleanValue();
        if (test) {
            return thenBlock.eval(virtualMachine);
        } else if (elseBlock != null) {
            return elseBlock.eval(virtualMachine);
        }
        return BaylBoolean.FALSE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append("if ");
        sb.append(testCondition);
        sb.append(' ');
        sb.append(thenBlock);
        if (elseBlock != null) {
            sb.append(' ');
            sb.append(elseBlock);
        }
        sb.append(')');
        return sb.toString();
    }
}
