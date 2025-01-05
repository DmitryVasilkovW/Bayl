package org.bayl.ast.operator.comparison;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;

public class NotEqualsOpNode extends RelationalOpNode {
    public NotEqualsOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "!=", left, right);
    }

    @Override
    public BaylObject eval(Interpreter interpreter) {
        return equals(interpreter).not();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);

        bytecode.add("NOT_EQUALS");
    }
}
