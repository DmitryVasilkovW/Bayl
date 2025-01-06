package org.bayl.ast.statement;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.impl.VirtualMachineImpl;

public class IfNode extends Node {

    private final Node testCondition;
    private final Node thenBlock;
    private final Node elseBlock;

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
