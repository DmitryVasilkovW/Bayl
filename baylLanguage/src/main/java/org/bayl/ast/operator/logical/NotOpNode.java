package org.bayl.ast.operator.logical;

import org.bayl.ast.BooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.UnaryOpNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.NOT;
import org.bayl.model.SourcePosition;

public class NotOpNode extends UnaryOpNode implements BooleanOpNode {

    public NotOpNode(SourcePosition pos, Node operand) {
        super(pos, "not", operand);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                NOT.toString()
        ));

        getOperand().generateCode(bytecode);
    }
}
