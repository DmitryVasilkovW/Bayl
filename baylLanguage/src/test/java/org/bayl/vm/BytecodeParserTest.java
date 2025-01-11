package org.bayl.vm;

import org.bayl.SourcePosition;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.RootExecutor;
import org.bayl.vm.executor.expression.array.ArrayExecutor;
import org.bayl.vm.executor.expression.literale.NumberExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.bayl.vm.impl.BytecodeParserImpl;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BytecodeParserTest {

    private final BytecodeParserImpl parser = new BytecodeParserImpl();

    @Test
    public void testParseArray() {
        List<String> bytecode = List.of(
                "BLOCK_START 1 1",
                "SET 1 3",
                "ARRAY_INIT 2 1 5",
                "ARRAY_STORE 0",
                "PUSH_N 1 1 6",
                "ARRAY_STORE 1",
                "PUSH_N 2 1 9",
                "ARRAY_END",
                "LOAD a 1 1",
                "BLOCK_END");

        var result = parser.parse(bytecode);

        var expected = getExpected(
                getAssign(1, 3,
                          getVar(1, 1, "a"),
                          getArray(1, 5, getNumber(1, 6, "1"), getNumber(1, 9, "2"))
        ));

        assertEquals(result, expected);
    }

    private RootExecutor getExpected(Executor... executor) {
        var position = new SourcePosition(1, 1);
        var statements = List.of(executor);

        return new RootExecutor(position, statements);
    }

    private ArrayExecutor getArray(int l, int c, Executor... elements) {
        return new ArrayExecutor(new SourcePosition(l, c), List.of(elements));
    }

    private NumberExecutor getNumber(int l, int c, String val) {
        return new NumberExecutor(new SourcePosition(l, c), val);
    }

    private AssignExecutor getAssign(int l, int c, Executor var, Executor expression) {
        return new AssignExecutor(new SourcePosition(l, c), var, expression);
    }

    private VariableExecutor getVar(int l, int c, String name) {
        return new VariableExecutor(new SourcePosition(l, c), name);
    }
}
