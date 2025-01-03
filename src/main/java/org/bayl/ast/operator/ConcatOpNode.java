package org.bayl.ast.operator;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.ZemObject;
import org.bayl.runtime.object.ZemString;

public class ConcatOpNode extends BinaryOpNode {
    public ConcatOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "~", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemString left = getLeft().eval(interpreter).toZString();
        ZemString right = getRight().eval(interpreter).toZString();
        return left.concat(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);

        bytecode.add("CONCAT");
    }
}
