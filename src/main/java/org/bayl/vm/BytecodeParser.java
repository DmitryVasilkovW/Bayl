package org.bayl.vm;

import org.bayl.vm.model.Instruction;
import java.util.List;

public interface BytecodeParser {
    List<Instruction> parse(List<String> bytecode);
}
