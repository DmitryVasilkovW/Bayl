package org.bayl.ast.operator.arithmetic;

import org.bayl.model.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.ArithmeticOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylNumber;
import static org.bayl.model.BytecodeToken.MULTIPLY;
import org.bayl.vm.Environment;

public class MultiplyOpNode extends BinaryOpNode implements ArithmeticOpNode {

    public MultiplyOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "*", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylNumber left = getLeft().eval(virtualMachine).toNumber(getLeft().getPosition());
        BaylNumber right = getRight().eval(virtualMachine).toNumber(getRight().getPosition());
        return left.multiply(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                MULTIPLY.toString())
        );

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
