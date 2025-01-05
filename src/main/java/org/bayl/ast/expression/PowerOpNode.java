package org.bayl.ast.expression;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.IArithmeticOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.BinaryOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.runtime.BaylObject;

public class PowerOpNode extends BinaryOpNode implements IArithmeticOpNode {
    public PowerOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "^", left, right);
    }

    @Override
    public BaylObject eval(Interpreter interpreter) {
        BaylNumber left = getLeft().eval(interpreter).toNumber(getLeft().getPosition());
        BaylNumber right = getRight().eval(interpreter).toNumber(getRight().getPosition());
        return left.power(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);

        bytecode.add("POWER");
    }
}
