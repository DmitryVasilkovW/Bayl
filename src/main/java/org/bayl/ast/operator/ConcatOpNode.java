package org.bayl.ast.operator;

import org.bayl.SourcePosition;
import org.bayl.ast.BinaryOpNode;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;
import org.bayl.vm.impl.VirtualMachineImpl;
import static org.bayl.model.BytecodeToken.CONCAT;

public class ConcatOpNode extends BinaryOpNode {

    public ConcatOpNode(SourcePosition pos, Node left, Node right) {
        super(pos, "~", left, right);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        BaylString left = getLeft().eval(virtualMachine).toBaylString();
        BaylString right = getRight().eval(virtualMachine).toBaylString();
        return left.concat(right);
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                CONCAT.toString()
        ));

        getLeft().generateCode(bytecode);
        getRight().generateCode(bytecode);
    }
}
