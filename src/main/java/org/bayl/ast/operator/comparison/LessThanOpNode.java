package org.bayl.ast.operator.comparison;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.impl.VirtualMachineImpl;
import static org.bayl.model.BytecodeToken.LESS_THAN;

public class LessThanOpNode extends RelationalOpNode {

    public LessThanOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "<", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return BaylBoolean.valueOf(compare(virtualMachine) < 0);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                LESS_THAN.toString())
        );

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
