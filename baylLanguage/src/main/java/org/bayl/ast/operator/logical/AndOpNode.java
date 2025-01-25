package org.bayl.ast.operator.logical;

import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.BooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.AND;
import org.bayl.model.SourcePosition;

public class AndOpNode extends BinaryOpNode implements BooleanOpNode {

    public AndOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "and", left, right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                AND.toString()
        ));

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
