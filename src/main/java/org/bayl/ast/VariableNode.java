package org.bayl.ast;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemObject;

public class VariableNode extends Node {
    private String name;

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
    public ZemObject eval(Interpreter interpreter) {
        return interpreter.getVariable(name, getPosition());
    }
}
