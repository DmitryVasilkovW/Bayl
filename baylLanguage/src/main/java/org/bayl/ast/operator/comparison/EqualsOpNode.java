package org.bayl.ast.operator.comparison;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.impl.VirtualMachineImpl;
import static org.bayl.model.BytecodeToken.EQUALS;

public class EqualsOpNode extends RelationalOpNode {

    public EqualsOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "==", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return equals(virtualMachine);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                EQUALS.toString())
        );

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
