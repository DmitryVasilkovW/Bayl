package util.bytecode;

import org.bayl.vm.executor.Executor;
import java.util.List;

public record BytecodeParserTestData(
        String message,
        List<String> bytecode,
        Executor expected
) {
}
