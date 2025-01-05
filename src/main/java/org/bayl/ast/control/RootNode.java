package org.bayl.ast.control;

import org.bayl.SourcePosition;
import org.bayl.ast.statement.BlockNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import java.util.List;

public class RootNode extends BlockNode {
    public RootNode(SourcePosition pos, List<Node> statements) {
        super(pos, statements);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node node : getStatements()) {
            sb.append(node.toString());
        }
        return sb.toString();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        super.generateCode(bytecode);
    }
}
