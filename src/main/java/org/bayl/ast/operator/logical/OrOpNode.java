package org.bayl.ast.operator.logical;

import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.IBooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.impl.VirtualMachineImpl;

public class OrOpNode extends BinaryOpNode implements IBooleanOpNode {

    public OrOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "or", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylBoolean left = getLeft().eval(virtualMachine).toBoolean(getLeft().getPosition());
        if (left.booleanValue()) {
            return left;
        }
        BaylBoolean right = getRight().eval(virtualMachine).toBoolean(getRight().getPosition());
        return left.or(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);

        bytecode.add("OR");
    }
}
