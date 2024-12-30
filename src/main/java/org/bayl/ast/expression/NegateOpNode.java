package org.bayl.ast.expression;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.IArithmeticOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.UnaryOpNode;
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
