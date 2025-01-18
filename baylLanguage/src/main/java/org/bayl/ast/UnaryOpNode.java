package org.bayl.ast;

import org.bayl.model.SourcePosition;

public abstract class UnaryOpNode extends Node {

    protected String operator;
    protected Node operand;

    public UnaryOpNode(SourcePosition pos, String operator, Node operand) {
        super(pos);
        this.operator = operator;
        this.operand = operand;
    }

    public String getName() {
        return operator;
    }

    public Node getOperand() {
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
