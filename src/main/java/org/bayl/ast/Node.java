package org.bayl.ast;

import org.bayl.SourcePosition;
import org.bayl.vm.impl.VirtualMachineImpl;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;

public abstract class Node {
    private SourcePosition position;

    public Node(SourcePosition position) {
        this.position = position;
    }

    public SourcePosition getPosition() {
        return position;
    }

    abstract public BaylObject eval(VirtualMachineImpl virtualMachine);

    public abstract void generateCode(Bytecode bytecode);
}
