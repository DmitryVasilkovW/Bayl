package org.bayl.ast.statement;

import java.util.List;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.runtime.ZemObject;

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
    public ZemObject eval(Interpreter interpreter) {
        ZemObject ret = null;
        for (Node statement : statements) {
            ret = statement.eval(interpreter);
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
}
