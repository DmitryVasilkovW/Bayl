package org.bayl.bytecode.impl;

import java.util.ArrayList;
import java.util.List;
import org.bayl.ast.control.RootNode;

public class Bytecode {

    private final List<String> instructions = new ArrayList<>();

    public void add(String instruction) {
        instructions.add(instruction);
    }

    public List<String> getInstructions(RootNode ast) {
        ast.generateCode(this);
        return instructions;
    }

    @Override
    public String toString() {
        return String.join("\n", instructions);
    }
}
