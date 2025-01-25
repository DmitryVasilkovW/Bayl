package org.bayl.ast.operator.string;

import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.CONCAT;
import org.bayl.model.SourcePosition;

public class ConcatOpNode extends BinaryOpNode {

    public ConcatOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "~", left, right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                CONCAT.toString()
        ));

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
