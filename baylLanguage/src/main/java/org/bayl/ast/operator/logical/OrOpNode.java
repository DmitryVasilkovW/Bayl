package org.bayl.ast.operator.logical;

import org.bayl.model.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.BooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import static org.bayl.model.BytecodeToken.OR;
import org.bayl.vm.Environment;

public class OrOpNode extends BinaryOpNode implements BooleanOpNode {

    public OrOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "or", left, right);
    }

    @Override
    public BaylObject eval(Environment virtualMachine) {
        BaylBoolean left = getLeft().eval(virtualMachine).toBoolean(getLeft().getPosition());
        if (left.booleanValue()) {
            return left;
        }
        BaylBoolean right = getRight().eval(virtualMachine).toBoolean(getRight().getPosition());
        return left.or(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                OR.toString()
        ));

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
