package org.bayl.vm.executor;

import lombok.EqualsAndHashCode;
import org.bayl.SourcePosition;

@EqualsAndHashCode(callSuper = true)
public abstract class BinaryOpExecutor extends Executor {

    protected String operator;
    protected Executor left;
    protected Executor right;

    protected BinaryOpExecutor(SourcePosition pos, String operator, Executor left, Executor right) {
        super(pos);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getName() {
        return operator;
    }

    public Executor getLeft() {
        return left;
    }

    public Executor getRight() {
        return right;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(getName());
        sb.append(' ');
        sb.append(left.toString());
        sb.append(' ');
        sb.append(right.toString());
        sb.append(')');
        return sb.toString();
    }
}
