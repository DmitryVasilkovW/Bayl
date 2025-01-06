package org.bayl.ast.operator.arithmetic;

import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.IArithmeticOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.runtime.BaylObject;

public class DivideOpNode extends BinaryOpNode implements IArithmeticOpNode {
    public DivideOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "/", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylNumber left = getLeft().eval(virtualMachine).toNumber(getLeft().getPosition());
        BaylNumber right = getRight().eval(virtualMachine).toNumber(getRight().getPosition());
        return left.divide(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);

        bytecode.add("DIVIDE");
    }
}
