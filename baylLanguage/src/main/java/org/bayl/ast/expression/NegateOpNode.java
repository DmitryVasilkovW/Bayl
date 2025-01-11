package org.bayl.ast.expression;

import org.bayl.SourcePosition;
import org.bayl.ast.ArithmeticOpNode;
import org.bayl.ast.Node;
import org.bayl.ast.UnaryOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.vm.impl.VirtualMachineImpl;
import static org.bayl.model.BytecodeToken.NEGATE;

public class NegateOpNode extends UnaryOpNode implements ArithmeticOpNode {

    public NegateOpNode(SourcePosition pos, Node value) {
        super(pos, "-", value);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylNumber operand = getOperand().eval(virtualMachine).toNumber(getOperand().getPosition());
        return operand.negate();
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                NEGATE.toString())
        );

        getOperand().generateCode(bytecode);
    }
}
