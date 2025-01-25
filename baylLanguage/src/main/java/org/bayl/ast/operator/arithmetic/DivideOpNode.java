package org.bayl.ast.operator.arithmetic;

import org.bayl.ast.ArithmeticOpNode;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.DIVIDE;
import org.bayl.model.SourcePosition;

public class DivideOpNode extends BinaryOpNode implements ArithmeticOpNode {

    public DivideOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "/", left, right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                DIVIDE.toString())
        );

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
