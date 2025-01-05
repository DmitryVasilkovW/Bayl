package org.bayl.ast.operator;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;

public class ConcatOpNode extends BinaryOpNode {
    public ConcatOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "~", left, right);
    }

    @Override
    public BaylObject eval(Interpreter interpreter) {
        BaylString left = getLeft().eval(interpreter).toZString();
        BaylString right = getRight().eval(interpreter).toZString();
        return left.concat(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);

        bytecode.add("CONCAT");
    }
}
