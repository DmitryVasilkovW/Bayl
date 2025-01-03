package org.bayl.ast.operator.logical;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.IBooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.UnaryOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.ZemBoolean;
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

    @Override
    public void generateCode(Bytecode bytecode) {
        getOperand().generateCode(bytecode);

        bytecode.add("NOT");
    }
}
