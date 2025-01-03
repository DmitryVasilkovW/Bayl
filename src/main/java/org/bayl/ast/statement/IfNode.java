package org.bayl.ast.statement;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class IfNode extends Node {
    private Node testCondition;
    private Node thenBlock;
    private Node elseBlock;

    public IfNode(SourcePosition pos, Node testCondition, Node thenBlock, Node elseBlock) {
        super(pos);
        this.testCondition = testCondition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public Node getTestCondition() {
        return testCondition;
    }

    public Node getThenBlock() {
        return thenBlock;
    }

    public Node getElseBlock() {
        return elseBlock;
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        boolean test = testCondition.eval(interpreter).toBoolean(testCondition.getPosition()).booleanValue();
        if (test) {
            return thenBlock.eval(interpreter);
        } else if (elseBlock != null) {
            return elseBlock.eval(interpreter);
        }
        return ZemBoolean.FALSE;
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

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("IF");
        bytecode.add("CONDITION");
        testCondition.generateCode(bytecode);
        bytecode.add("THEN");
        thenBlock.generateCode(bytecode);

        if (elseBlock != null) {
            bytecode.add("ELSE");
            elseBlock.generateCode(bytecode);
        }
    }
}
