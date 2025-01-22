package org.bayl.ast.operator.logical;

import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.BooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.OR;
import org.bayl.model.SourcePosition;

public class OrOpNode extends BinaryOpNode implements BooleanOpNode {

    public OrOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "or", left, right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                OR.toString()
        ));

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
