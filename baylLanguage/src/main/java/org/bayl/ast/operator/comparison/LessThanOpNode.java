package org.bayl.ast.operator.comparison;

import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.LESS_THAN;
import org.bayl.model.SourcePosition;

public class LessThanOpNode extends RelationalOpNode {

    public LessThanOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "<", left, right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                LESS_THAN.toString())
        );

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
