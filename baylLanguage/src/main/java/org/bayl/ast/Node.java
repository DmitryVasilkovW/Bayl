package org.bayl.ast;

import org.bayl.SourcePosition;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.impl.VirtualMachineImpl;

public abstract class Node {

    private final SourcePosition position;

    public Node(SourcePosition position) {
        this.position = position;
    }

    public SourcePosition getPosition() {
        return position;
    }

    public String getBytecodeLineWithPosition(String... tokens) {
        String line = getBytecodeLine(tokens);

        return getBytecodeLine(
                line,
                getPositionForBytecode()
        );
    }

    public String getBytecodeLine(String... tokens) {
        return String.join(" ", tokens);
    }

    public String getPositionForBytecode() {
        return getPosition().getLineNumber() + " " + getPosition().getColumnNumber();
    }

    public abstract BaylObject eval(VirtualMachineImpl virtualMachine);

    public abstract void generateCode(Bytecode bytecode);
}
