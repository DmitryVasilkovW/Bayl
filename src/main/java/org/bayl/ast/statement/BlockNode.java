package org.bayl.ast.statement;

import java.util.List;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;

public class BlockNode extends Node {
    private List<Node> statements;

    public BlockNode(SourcePosition pos, List<Node> statements) {
        super(pos);
        this.statements = statements;
    }

    public Node get(int index) {
        return statements.get(index);
    }

    protected List<Node> getStatements() {
        return statements;
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylObject ret = null;
        for (Node statement : statements) {
            ret = statement.eval(virtualMachine);
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Node node : statements) {
            sb.append(node.toString());
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("BLOCK_START");

        for (Node statement : statements) {
            statement.generateCode(bytecode);
        }

        bytecode.add("BLOCK_END");
    }
}
