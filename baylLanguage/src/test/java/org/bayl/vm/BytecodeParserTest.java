package org.bayl.vm;

import org.bayl.SourcePosition;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.control.RootExecutor;
import org.bayl.vm.executor.expression.array.ArrayExecutor;
import org.bayl.vm.executor.expression.array.DictionaryEntryExecutor;
import org.bayl.vm.executor.expression.array.DictionaryExecutor;
import org.bayl.vm.executor.expression.array.LookupExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.literale.NumberExecutor;
import org.bayl.vm.executor.expression.literale.StringExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.operator.arithmetic.AddOpExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.bayl.vm.executor.statement.ReturnExecutor;
import org.bayl.vm.impl.BytecodeParserImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.TestData;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BytecodeParserTest {

    private final BytecodeParserImpl parser = new BytecodeParserImpl();

    @ParameterizedTest(name = "{index} => {arguments}")
    @DisplayName("Test parsing bytecode: {0}")
    @MethodSource("getAllTestData")
    public void testBytecodeParser(TestData test) {
        Executor result = parser.parse(test.bytecode());
        assertEquals(result, test.expected(), test.message());
    }

    private List<TestData> getAllTestData() {
        return List.of(
                new TestData(
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "ARRAY_INIT 2 1 5\n" +
                                            "ARRAY_STORE 0\n" +
                                            "PUSH_N 1 1 6\n" +
                                            "ARRAY_STORE 1\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "ARRAY_END\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(
                                getAssign(1, 3,
                                          getVar(1, 1, "a"),
                                          getArray(1, 5, getNumber(1, 6, "1"), getNumber(1, 9, "2")))
                        ),
                        "test parsing array"
                ),
                new TestData(
                        getBytecode("BLOCK_START 1 1\n" +
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
                                            "BLOCK_END"),
                        getExpected(
                                getAssign(1, 3,
                                          getVar(1, 1, "a"),
                                          getDict(1, 5,
                                                  getPair(1, 10, getString(1, 6, "1"), getNumber(1, 12, "1")),
                                                  getPair(1, 19, getString(1, 15, "3"), getNumber(1, 21, "3"))))
                        ),
                        "test parsing dictionary"
                ),
                new TestData(
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "ARRAY_INIT 3 1 5\n" +
                                            "ARRAY_STORE 0\n" +
                                            "PUSH_N 2 1 6\n" +
                                            "ARRAY_STORE 1\n" +
                                            "PUSH_N 3 1 9\n" +
                                            "ARRAY_STORE 2\n" +
                                            "PUSH_N 8 1 12\n" +
                                            "ARRAY_END\n" +
                                            "LOAD a 1 1\n" +
                                            "SET 2 6\n" +
                                            "PUSH_N 9 2 8\n" +
                                            "LOOKUP 2 2\n" +
                                            "LOAD a 2 1\n" +
                                            "PUSH_N 2 2 3\n" +
                                            "BLOCK_END"),
                        getExpected(
                                getAssign(1, 3,
                                          getVar(1, 1, "a"),
                                          getArray(1, 5, getNumber(1, 6, "2"), getNumber(1, 9, "3"), getNumber(1, 12, "8"))
                                ),
                                getAssign(2, 6,
                                          getLookup(2, 2, getVar(2, 1, "a"), getNumber(2, 3, "2")),
                                          getNumber(2, 8, "9"))
                        ),
                        "test parsing lookup"
                ),
                new TestData(
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 5\n" +
                                            "FUNC 1 7\n" +
                                            "ARG\n" +
                                            "LOAD a 1 16\n" +
                                            "ARG\n" +
                                            "LOAD b 1 19\n" +
                                            "BODY\n" +
                                            "BLOCK_START 1 22\n" +
                                            "SET 2 7\n" +
                                            "ADD 2 11\n" +
                                            "LOAD a 2 9\n" +
                                            "LOAD b 2 13\n" +
                                            "LOAD c 2 5\n" +
                                            "RETURN 3 5\n" +
                                            "ADD 3 14\n" +
                                            "LOAD c 3 12\n" +
                                            "PUSH_N 1 3 16\n" +
                                            "BLOCK_END\n" +
                                            "LOAD sum 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 5,
                                              getVar(1, 1, "sum"),
                                              getFunction(1, 7,
                                                          List.of(getVar(1, 16, "a"), getVar(1, 19, "b")),
                                                          getBlock(1, 22,
                                                                  getAssign(2, 7,
                                                                            getVar(2, 5, "c"),
                                                                            getAdd(2, 11,
                                                                                   getVar(2, 9, "a"),
                                                                                   getVar(2, 13, "b"))),
                                                                   getReturn(3, 5,
                                                                             getAdd(3, 14,
                                                                                    getVar(3, 12, "c"),
                                                                                    getNumber(3, 16, "1"))))))
                        ),
                        "test parsing function with two parameters"
                )
        );
    }

    private RootExecutor getExpected(Executor... executor) {
        var position = new SourcePosition(1, 1);
        var statements = List.of(executor);

        return new RootExecutor(position, statements);
    }

    private BlockExecutor getBlock(int l, int c, Executor... executor) {
        var position = new SourcePosition(l, c);
        var statements = List.of(executor);

        return new BlockExecutor(position, statements);
    }

    private ReturnExecutor getReturn(int l, int c, Executor exp) {
        return new ReturnExecutor(new SourcePosition(l, c), exp);
    }

    private FunctionExecutor getFunction(int l, int c, List<Executor> params, Executor body) {
        return new FunctionExecutor(new SourcePosition(l, c), params, body);
    }

    private AddOpExecutor getAdd(int l, int c, Executor le, Executor r) {
        return new AddOpExecutor(new SourcePosition(l, c), le, r);
    }

    private LookupExecutor getLookup(int l, int c, VariableExecutor var, Executor key) {
        return new LookupExecutor(new SourcePosition(l, c), var, key);
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
