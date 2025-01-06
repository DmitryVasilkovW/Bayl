package org.bayl.ast.expression.variable;

import org.bayl.SourcePosition;
import org.bayl.ast.Node;
import org.bayl.bytecode.Bytecode;
import org.bayl.runtime.BaylObject;
import org.bayl.vm.impl.VirtualMachineImpl;

public class VariableNode extends Node {

    private final String name;

    public VariableNode(SourcePosition pos, String variableName) {
        super(pos);
        name = variableName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public BaylObject eval(VirtualMachineImpl virtualMachine) {
        return virtualMachine.getVariable(name, getPosition());
    }

    @Override
    public void generateCode(Bytecode bytecode) {
        bytecode.add("LOAD " + name);
    }
}
