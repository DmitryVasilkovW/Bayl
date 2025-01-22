package org.bayl.ast.operator.arithmetic;

import org.bayl.ast.ArithmeticOpNode;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import static org.bayl.model.BytecodeToken.SUBTRACT;
import org.bayl.model.SourcePosition;

public class SubtractOpNode extends BinaryOpNode implements ArithmeticOpNode {

    public SubtractOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "-", left, right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                SUBTRACT.toString())
        );

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
