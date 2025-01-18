package org.bayl.vm.executor;

import lombok.EqualsAndHashCode;
import org.bayl.model.SourcePosition;

@EqualsAndHashCode(callSuper = true)
public abstract class UnaryOpExecutor extends Executor {

    protected String operator;
    protected Executor operand;

    public UnaryOpExecutor(SourcePosition pos, String operator, Executor operand) {
        super(pos);
        this.operator = operator;
        this.operand = operand;
    }

    public String getName() {
        return operator;
    }

    public Executor getOperand() {
        return operand;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(getName());
        sb.append(' ');
        sb.append(operand.toString());
        sb.append(')');
        return sb.toString();
    }

}
