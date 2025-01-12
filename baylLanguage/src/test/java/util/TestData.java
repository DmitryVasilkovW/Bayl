package util;

import org.bayl.vm.executor.Executor;
import java.util.List;

public record TestData(
        List<String> bytecode,
        Executor expected,
        String message) {
}
