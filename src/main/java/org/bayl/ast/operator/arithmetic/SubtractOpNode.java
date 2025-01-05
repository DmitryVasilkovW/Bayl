package org.bayl.ast.operator.arithmetic;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.IArithmeticOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.ZemNumber;
import org.bayl.runtime.ZemObject;

public class SubtractOpNode extends BinaryOpNode implements IArithmeticOpNode {
    public SubtractOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "-", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemNumber left = getLeft().eval(interpreter).toNumber(getLeft().getPosition());
        ZemNumber right = getRight().eval(interpreter).toNumber(getRight().getPosition());
        return left.subtract(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);

        bytecode.add("SUBTRACT");
    }
}
