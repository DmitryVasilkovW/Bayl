package org.bayl.ast.operator.logical;

import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.BooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.impl.VirtualMachineImpl;
import static org.bayl.model.BytecodeToken.AND;

public class AndOpNode extends BinaryOpNode implements BooleanOpNode {

    public AndOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "and", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylBoolean left = getLeft().eval(virtualMachine).toBoolean(getLeft().getPosition());
        if (!left.booleanValue()) {
            return left;
        }
        BaylBoolean right = getRight().eval(virtualMachine).toBoolean(getRight().getPosition());
        return left.and(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                AND.toString()
        ));

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
