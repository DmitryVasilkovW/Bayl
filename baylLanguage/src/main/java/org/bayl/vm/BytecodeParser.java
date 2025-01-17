package org.bayl.vm;

import java.util.List;
import org.bayl.vm.executor.control.RootExecutor;

public interface BytecodeParser {

    RootExecutor parse(List<String> bytecode);
}
