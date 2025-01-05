package org.bayl.ast.operator.logical;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.runtime.BaylObject;

public class GreaterEqualOpNode extends RelationalOpNode {
    public GreaterEqualOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, ">=", left, right);
    }

    @Override
    public BaylObject eval(Interpreter interpreter) {
        return BaylBoolean.valueOf(compare(interpreter) >= 0);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);

        getRight().generateCode(bytecode);

        bytecode.add("GREATER_EQUAL");
    }
}
