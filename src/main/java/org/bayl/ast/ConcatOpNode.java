package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemObject;
import org.bayl.runtime.ZemString;

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
}
