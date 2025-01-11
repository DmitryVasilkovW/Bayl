package org.bayl.ast.expression;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.impl.VirtualMachineImpl;
import static org.bayl.model.BytecodeToken.PUSH;

public class FalseNode extends Node {

    private static final String VALUE = "false";

    public FalseNode(SourcePosition pos) {
        super(pos);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return BaylBoolean.FALSE;
    }

    @Override
    public String toString() {
        return VALUE;
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add(getBytecodeLineWithPosition(
                PUSH.toString(),
                VALUE)
        );
    }
}
