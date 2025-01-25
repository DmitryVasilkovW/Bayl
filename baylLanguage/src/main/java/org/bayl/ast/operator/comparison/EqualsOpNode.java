package org.bayl.ast.operator.comparison;

import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.EQUALS;
import org.bayl.model.SourcePosition;

public class EqualsOpNode extends RelationalOpNode {

    public EqualsOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "==", left, right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                EQUALS.toString())
        );

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
