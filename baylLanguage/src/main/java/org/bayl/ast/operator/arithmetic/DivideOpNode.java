package org.bayl.ast.operator.arithmetic;

import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.ArithmeticOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylNumber;
import static org.bayl.model.BytecodeToken.DIVIDE;
import org.bayl.vm.Environment;

public class DivideOpNode extends BinaryOpNode implements ArithmeticOpNode {

    public DivideOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "/", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylNumber left = getLeft().eval(virtualMachine).toNumber(getLeft().getPosition());
        BaylNumber right = getRight().eval(virtualMachine).toNumber(getRight().getPosition());
        return left.divide(right);
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
