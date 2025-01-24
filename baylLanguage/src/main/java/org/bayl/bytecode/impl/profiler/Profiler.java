package org.bayl.bytecode.impl.profiler;

import lombok.Getter;
import org.bayl.model.BytecodeToken;

import java.util.HashMap;
import java.util.Map;

public class Profiler {

    private final int threshold;
    private final Map<BytecodeToken, Integer> instructions = new HashMap<>();

    @Getter
    private final Map<BytecodeToken, BytecodeToken> thresholdViolators = new HashMap<>();

    private Profiler(int threshold) {
        this.threshold = threshold;
    }

    private Profiler() {
        this(5);
    }

    private static class ProfilerHolder {
        private static final Profiler INSTANCE = new Profiler();
    }

    public static Profiler getInstance() {
        return ProfilerHolder.INSTANCE;
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
