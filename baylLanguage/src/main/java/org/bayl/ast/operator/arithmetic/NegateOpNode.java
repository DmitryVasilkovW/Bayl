package org.bayl.ast.operator.arithmetic;

import org.bayl.ast.ArithmeticOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.UnaryOpNode;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.NEGATE;
import org.bayl.model.SourcePosition;

public class NegateOpNode extends UnaryOpNode implements ArithmeticOpNode {

    public NegateOpNode(SourcePosition pos, Node value) {
        super(pos, "-", value);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                NEGATE.toString())
        );

        getOperand().generateCode(bytecode);
    }
}
