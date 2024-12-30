package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemNumber;
import org.bayl.runtime.ZemObject;

public class NegateOpNode extends UnaryOpNode implements IArithmeticOpNode {
    public NegateOpNode(SourcePosition pos, Node value) {
        super(pos, "-", value);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemNumber operand = getOperand().eval(interpreter).toNumber(getOperand().getPosition());
        return operand.negate();
    }
}
