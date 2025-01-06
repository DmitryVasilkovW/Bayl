package org.bayl.ast.operator.logical;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.ast.IBooleanOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.UnaryOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.runtime.BaylObject;

public class NotOpNode extends UnaryOpNode implements IBooleanOpNode {
    public NotOpNode(SourcePosition pos, Node operand) {
        super(pos, "not", operand);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylBoolean operand = getOperand().eval(virtualMachine).toBoolean(getOperand().getPosition());
        return operand.not();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getOperand().generateCode(bytecode);

        bytecode.add("NOT");
    }
}
