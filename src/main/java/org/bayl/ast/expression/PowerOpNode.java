package org.bayl.ast.expression;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.ast.IArithmeticOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.BinaryOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.runtime.BaylObject;

public class PowerOpNode extends BinaryOpNode implements IArithmeticOpNode {
    public PowerOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "^", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylNumber left = getLeft().eval(virtualMachine).toNumber(getLeft().getPosition());
        BaylNumber right = getRight().eval(virtualMachine).toNumber(getRight().getPosition());
        return left.power(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);

        bytecode.add("POWER");
    }
}
