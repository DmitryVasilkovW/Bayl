package org.bayl.vm;

import org.bayl.SourcePosition;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.RootExecutor;
import org.bayl.vm.executor.expression.array.ArrayExecutor;
import org.bayl.vm.executor.expression.array.DictionaryEntryExecutor;
import org.bayl.vm.executor.expression.array.DictionaryExecutor;
import org.bayl.vm.executor.expression.literale.NumberExecutor;
import org.bayl.vm.executor.expression.literale.StringExecutor;
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
        var bytecode = getBytecode("BLOCK_START 1 1\n" +
                                           "SET 1 3\n" +
                                           "ARRAY_INIT 2 1 5\n" +
                                           "ARRAY_STORE 0\n" +
                                           "PUSH_N 1 1 6\n" +
                                           "ARRAY_STORE 1\n" +
                                           "PUSH_N 2 1 9\n" +
                                           "ARRAY_END\n" +
                                           "LOAD a 1 1\n" +
                                           "BLOCK_END");

        var result = parser.parse(bytecode);

        var expected = getExpected(
                getAssign(1, 3,
                          getVar(1, 1, "a"),
                          getArray(1, 5, getNumber(1, 6, "1"), getNumber(1, 9, "2"))
        ));

        assertEquals(result, expected);
    }

    @Test
    public void testDictionary() {
        var bytecode = getBytecode("BLOCK_START 1 1\n" +
                                           "SET 1 3\n" +
                                           "DICT_INIT 2 1 5\n" +
                                           "DICT_PAIR 1 10\n" +
                                           "PUSH_S 1 1 6\n" +
                                           "PUSH_N 1 1 12\n" +
                                           "DICT_PAIR 1 19\n" +
                                           "PUSH_S 3 1 15\n" +
                                           "PUSH_N 3 1 21\n" +
                                           "DICT_END\n" +
                                           "LOAD a 1 1\n" +
                                           "BLOCK_END");

        var result = parser.parse(bytecode);

        var expected = getExpected(
                getAssign(1, 3,
                          getVar(1, 1, "a"),
                          getDict(1, 5,
                                  getPair(1, 10, getString(1, 6, "1"), getNumber(1, 12, "1")),
                                  getPair(1, 10, getString(1, 15, "3"), getNumber(1, 21, "3")))
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

    private StringExecutor getString(int l, int c, String val) {
        return new StringExecutor(new SourcePosition(l, c), val);
    }

    private AssignExecutor getAssign(int l, int c, Executor var, Executor expression) {
        return new AssignExecutor(new SourcePosition(l, c), var, expression);
    }

    private VariableExecutor getVar(int l, int c, String name) {
        return new VariableExecutor(new SourcePosition(l, c), name);
    }

    private DictionaryExecutor getDict(int l, int c, DictionaryEntryExecutor... pairs) {
        return new DictionaryExecutor(new SourcePosition(l, c), List.of(pairs));
    }

    private DictionaryEntryExecutor getPair(int l, int c, Executor key, Executor val) {
        return new DictionaryEntryExecutor(new SourcePosition(l, c), key, val);
    }

    private List<String> getBytecode(String code) {
        return List.of(code.split("\n"));
    }
}
