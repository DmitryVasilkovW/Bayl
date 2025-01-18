package org.bayl.ast.operator.comparison;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import static org.bayl.model.BytecodeToken.NOT_EQUALS;
import org.bayl.vm.Environment;

public class NotEqualsOpNode extends RelationalOpNode {

    public NotEqualsOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "!=", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return equals(virtualMachine).not();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                NOT_EQUALS.toString()
        ));

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
