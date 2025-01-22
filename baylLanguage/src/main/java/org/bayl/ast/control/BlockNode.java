package org.bayl.ast.control;

import org.bayl.model.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.model.BytecodeToken;
import org.bayl.runtime.BaylObject;
import java.util.List;
import java.util.stream.Stream;
import static org.bayl.model.BytecodeToken.BLOCK_START;
import org.bayl.vm.Environment;

public class BlockNode extends Node {

    private final List<Node> statements;

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
        bytecode.add(getBytecodeLineWithPosition(
                BLOCK_START.toString()
        ));

        for (Node statement : statements) {
            statement.generateCode(bytecode);
        }

        String finishLine = getBytecodeLine(
                BytecodeToken.BLOCK_END.toString()
        );
        bytecode.add(finishLine);
    }
}
