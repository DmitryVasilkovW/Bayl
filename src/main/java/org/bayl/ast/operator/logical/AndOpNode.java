package org.bayl.ast.operator.logical;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.IBooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class AndOpNode extends BinaryOpNode implements IBooleanOpNode {
    public AndOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "and", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemBoolean left = getLeft().eval(interpreter).toBoolean(getLeft().getPosition());
        if (!left.booleanValue()) {
            return left;
        }
        ZemBoolean right = getRight().eval(interpreter).toBoolean(getRight().getPosition());
        return left.and(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);

        String skipRightLabel = "SKIP_RIGHT_" + bytecode.getInstructions().size();
        bytecode.add("JUMP_IF_FALSE " + skipRightLabel);

        getRight().generateCode(bytecode);

        bytecode.add("AND");

        bytecode.add(skipRightLabel + ":");
    }
}
