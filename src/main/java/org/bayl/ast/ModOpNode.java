package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemNumber;
import org.bayl.runtime.ZemObject;

public class ModOpNode extends BinaryOpNode implements IArithmeticOpNode {
    public ModOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "%", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemNumber left = getLeft().eval(interpreter).toNumber(getLeft().getPosition());
        ZemNumber right = getRight().eval(interpreter).toNumber(getRight().getPosition());
        return left.remainder(right);
    }
}
