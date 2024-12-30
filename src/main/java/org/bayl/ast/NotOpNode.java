package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class NotOpNode extends UnaryOpNode implements IBooleanOpNode {
    public NotOpNode(SourcePosition pos, Node operand) {
        super(pos, "not", operand);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemBoolean operand = getOperand().eval(interpreter).toBoolean(getOperand().getPosition());
        return operand.not();
    }
}
