package org.bayl.ast.statement;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;

public class WhileNode extends Node {
    private Node testCondition;
    private Node loopBody;

    public WhileNode(SourcePosition pos, Node testCondition, Node loopBody) {
        super(pos);
        this.testCondition = testCondition;
        this.loopBody = loopBody;
    }

    public Node getTestCondition() {
        return testCondition;
    }

    public Node getLoopBody() {
        return loopBody;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
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

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("WHILE");
        bytecode.add("CONDITION");
        testCondition.generateCode(bytecode);
        bytecode.add("BODY");
        loopBody.generateCode(bytecode);
    }
}
