package org.bayl.bytecode;

import java.util.ArrayList;
import java.util.List;

public class Bytecode {
    private final List<String> instructions = new ArrayList<>();

    public void add(String instruction) {
        instructions.add(instruction);
    }

    public List<String> getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        return String.join("\n", instructions);
    }
}
