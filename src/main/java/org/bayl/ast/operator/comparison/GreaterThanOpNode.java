package org.bayl.ast.operator.comparison;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.ast.RelationalOpNode;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.impl.VirtualMachineImpl;

public class GreaterThanOpNode extends RelationalOpNode {

    public GreaterThanOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, ">", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return BaylBoolean.valueOf(compare(virtualMachine) > 0);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);

        bytecode.add("GREATER_THAN");
    }
}
