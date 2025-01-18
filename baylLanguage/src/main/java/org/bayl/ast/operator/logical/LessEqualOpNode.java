package org.bayl.ast.operator.logical;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import static org.bayl.model.BytecodeToken.LESS_EQUAL;
import org.bayl.vm.Environment;

public class LessEqualOpNode extends RelationalOpNode {

    public LessEqualOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "<=", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        return BaylBoolean.valueOf(compare(virtualMachine) <= 0);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                LESS_EQUAL.toString()
        ));

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
