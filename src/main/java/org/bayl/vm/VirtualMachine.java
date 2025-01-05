package org.bayl.vm;

import java.util.List;

public interface VirtualMachine {
    void run(List<String> bytecode);
}
