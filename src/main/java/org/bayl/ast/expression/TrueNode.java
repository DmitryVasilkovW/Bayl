package org.bayl.ast.expression;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.model.BytecodeToken;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylBoolean;
import org.bayl.vm.impl.VirtualMachineImpl;

public class TrueNode extends Node {

    public static final String VALUE = "true";

    public TrueNode(SourcePosition pos) {
        super(pos);
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return BaylBoolean.TRUE;
    }

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        String line = getBytecodeLine(
                BytecodeToken.PUSH.toString(), VALUE, getPositionForBytecode()
        );

        bytecode.add(line);
    }
}
