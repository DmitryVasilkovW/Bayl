package org.bayl.ast.operator.comparison;

import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.GREATER_THAN;
import org.bayl.model.SourcePosition;

public class GreaterThanOpNode extends RelationalOpNode {

    public GreaterThanOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, ">", left, right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                GREATER_THAN.toString())
        );

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
