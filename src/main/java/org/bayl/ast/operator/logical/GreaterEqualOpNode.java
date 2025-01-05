package org.bayl.ast.operator.logical;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.ZemBoolean;
import org.bayl.runtime.ZemObject;

public class GreaterEqualOpNode extends RelationalOpNode {
    public GreaterEqualOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, ">=", left, right);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return ZemBoolean.valueOf(compare(interpreter) >= 0);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);

        getRight().generateCode(bytecode);

        bytecode.add("GREATER_EQUAL");
    }
}
