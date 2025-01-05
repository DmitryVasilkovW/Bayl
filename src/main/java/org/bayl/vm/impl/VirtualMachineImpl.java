package org.bayl.vm.impl;

import org.bayl.vm.VirtualMachine;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class VirtualMachineImpl implements VirtualMachine {
    private final Stack<Object> stack = new Stack<>();
    private final Map<String, Object> globals = new HashMap<>();
    private int instructionPointer = 0;

    @Override
    public void run(List<String> bytecode) {
        while (instructionPointer < bytecode.size()) {
            String[] parts = bytecode.get(instructionPointer).split(" ");
            String instruction = parts[0];
            switch (instruction) {
                case "PUSH":
                    stack.push(parseValue(parts[1]));
                    break;
                case "STORE":
                    globals.put(parts[1], stack.pop());
                    break;
                case "LOAD":
                    stack.push(globals.get(parts[1]));
                    break;
                case "ADD":
                    double b = popNumber();
                    double a = popNumber();
                    stack.push(a + b);
                    break;
                case "SUBTRACT":
                    b = popNumber();
                    a = popNumber();
                    stack.push(a - b);
                    break;
                case "CALL":
                    String functionName = parts[1];
                    callFunction(functionName);
                    break;
                case "RETURN":
                    return;
                default:
                    throw new RuntimeException("Unknown instruction: " + instruction);
            }
            instructionPointer++;
        }
    }

    private Object parseValue(String value) {
        if (value.matches("-?\\d+(\\.\\d+)?")) {
            return Double.parseDouble(value);
        }
        if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        }
        return value; // строки
    }

    private double popNumber() {
        Object value = stack.pop();
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new RuntimeException("Expected a number, got: " + value);
    }

    private void callFunction(String functionName) {
        if ("println".equals(functionName)) {
            System.out.println(stack.pop());
        } else {
            throw new RuntimeException("Unknown function: " + functionName);
        }
    }

    public static void main(String[] args) {
        List<String> bytecode = Arrays.asList(
                "PUSH 3",
                "PUSH 2",
                "ADD",
                "CALL println",
                "PUSH 5",
                "STORE x",
                "LOAD x",
                "CALL println"
        );

        VirtualMachineImpl vm = new VirtualMachineImpl();
        vm.run(bytecode);
    }
}
