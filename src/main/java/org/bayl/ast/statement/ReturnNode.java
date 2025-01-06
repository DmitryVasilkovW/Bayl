package org.bayl.ast.statement;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.exception.ReturnException;
import org.bayl.runtime.BaylObject;

public class ReturnNode extends Node {
    private Node expression;

    public ReturnNode(SourcePosition pos, Node expression) {
        super(pos);
        this.expression = expression;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        throw new ReturnException(expression.eval(virtualMachine));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(return ");
        sb.append(expression);
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("RETURN");
        expression.generateCode(bytecode);
    }
}
