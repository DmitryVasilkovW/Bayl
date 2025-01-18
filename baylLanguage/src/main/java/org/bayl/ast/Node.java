package org.bayl.ast;

import org.bayl.model.SourcePosition;
import org.bayl.bytecode.impl.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.Environment;

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

    public abstract BaylObject eval(Environment virtualMachine);

    public abstract void generateCode(Bytecode bytecode);
}
