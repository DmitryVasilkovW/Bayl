package org.bayl.vm.impl;

import org.bayl.vm.BytecodeParser;
import org.bayl.vm.model.Instruction;
import java.util.ArrayList;
import java.util.List;

public class BytecodeParserImpl implements BytecodeParser {
    private static final String ARGS_DIVISION = " ";

    @Override
    public List<Instruction> parse(List<String> bytecode) {
        var instructions = new ArrayList<Instruction>();

        for (String line : bytecode) {
            instructions.add(parseInstruction(splitLine(line)));
        }

        return instructions;
    }

    private String[] splitLine(String line) {
        return line.split(ARGS_DIVISION);
    }

    private Instruction parseInstruction(String[] line) {
        for (String arg : line) {
            switch (arg) {
                case "ADD":
            }
        }

        return ;
    }
}
