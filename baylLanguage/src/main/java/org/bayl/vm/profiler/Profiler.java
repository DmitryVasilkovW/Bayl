package org.bayl.vm.profiler;

import org.bayl.model.BytecodeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profiler {

    private final int threshold;
    private final Map<BytecodeToken, Integer> instructions = new HashMap<>();
    private final Map<BytecodeToken, BytecodeToken> thresholdViolators = new HashMap<>();

    public Profiler(int threshold) {
        this.threshold = threshold;
    }

    public List<BytecodeToken> getInstructions() {
        return thresholdViolators.values().stream().toList();
    }

    public void countInstruction(BytecodeToken instruction) {
        int newCount = instructions.getOrDefault(instruction, 0) + 1;
        instructions.put(instruction, newCount);
        tryToAdd(instruction);
    }

    private void tryToAdd(BytecodeToken instruction) {
        if (instructions.get(instruction) >= threshold
                && !thresholdViolators.containsKey(instruction)) {
            thresholdViolators.put(instruction, instruction);
        }
    }
}
