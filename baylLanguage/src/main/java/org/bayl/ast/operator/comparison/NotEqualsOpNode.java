package org.bayl.ast.operator.comparison;

import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.NOT_EQUALS;
import org.bayl.model.SourcePosition;

public class NotEqualsOpNode extends RelationalOpNode {

    public NotEqualsOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "!=", left, right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                NOT_EQUALS.toString()
        ));

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
