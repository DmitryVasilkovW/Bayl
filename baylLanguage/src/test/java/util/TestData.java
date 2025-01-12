package util;

import org.bayl.vm.executor.Executor;
import java.util.List;

public record TestData(
        String message,
        List<String> bytecode,
        Executor expected
) {
}
