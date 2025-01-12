package org.bayl.vm;

import org.bayl.SourcePosition;
import org.bayl.vm.executor.Executor;
import org.bayl.vm.executor.control.BlockExecutor;
import org.bayl.vm.executor.control.RootExecutor;
import org.bayl.vm.executor.expression.FalseExecutor;
import org.bayl.vm.executor.expression.ModOpExecutor;
import org.bayl.vm.executor.expression.NegateOpExecutor;
import org.bayl.vm.executor.expression.PowerOpExecutor;
import org.bayl.vm.executor.expression.TrueExecutor;
import org.bayl.vm.executor.expression.array.ArrayExecutor;
import org.bayl.vm.executor.expression.array.DictionaryEntryExecutor;
import org.bayl.vm.executor.expression.array.DictionaryExecutor;
import org.bayl.vm.executor.expression.array.LookupExecutor;
import org.bayl.vm.executor.expression.function.FunctionCallExecutor;
import org.bayl.vm.executor.expression.function.FunctionExecutor;
import org.bayl.vm.executor.expression.literale.NumberExecutor;
import org.bayl.vm.executor.expression.literale.StringExecutor;
import org.bayl.vm.executor.expression.variable.VariableExecutor;
import org.bayl.vm.executor.operator.ConcatOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.AddOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.DivideOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.MultiplyOpExecutor;
import org.bayl.vm.executor.operator.arithmetic.SubtractOpExecutor;
import org.bayl.vm.executor.operator.comparison.EqualsOpExecutor;
import org.bayl.vm.executor.operator.comparison.GreaterThanOpExecutor;
import org.bayl.vm.executor.operator.comparison.LessThanOpExecutor;
import org.bayl.vm.executor.operator.comparison.NotEqualsOpExecutor;
import org.bayl.vm.executor.operator.logical.AndOpExecutor;
import org.bayl.vm.executor.operator.logical.GreaterEqualOpExecutor;
import org.bayl.vm.executor.operator.logical.LessEqualOpExecutor;
import org.bayl.vm.executor.operator.logical.NotOpExecutor;
import org.bayl.vm.executor.operator.logical.OrOpExecutor;
import org.bayl.vm.executor.statement.AssignExecutor;
import org.bayl.vm.executor.statement.ReturnExecutor;
import org.bayl.vm.impl.BytecodeParserImpl;
import org.junit.jupiter.api.DisplayName;
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
                        "test parsing array",
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
                                          getArray(1, 5, getNumber(1, 6, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing dictionary",
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
                                                  getPair(1, 19, getString(1, 15, "3"), getNumber(1, 21, "3")))))
                ),
                new TestData(
                        "test parsing lookup",
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
                                          getNumber(2, 8, "9")))
                ),
                new TestData(
                        "test parsing function with two parameters",
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
                                                                                    getNumber(3, 16, "1")))))))
                ),
                new TestData(
                        "test parsing calling function",
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
                                            "CALL print 6 1\n" +
                                            "LOAD print 6 1\n" +
                                            "ARG\n" +
                                            "CALL sum 6 7\n" +
                                            "LOAD sum 6 7\n" +
                                            "ARG\n" +
                                            "PUSH_N 1 6 11\n" +
                                            "ARG\n" +
                                            "PUSH_N 2 6 14\n" +
                                            "CALL_END\n" +
                                            "CALL_END\n" +
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
                                                                                    getNumber(3, 16, "1")))))),
                                    getCall(6, 1,
                                            getVar(6, 1, "print"),
                                            List.of(getCall(6, 7,
                                                            getVar(6, 7, "sum"),
                                                            List.of(getNumber(6, 11, "1"), getNumber(6, 14, "2"))))))
                ),
                new TestData(
                        "test parsing assign string",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "PUSH_S A 1 5\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getString(1, 5, "A")))
                ),
                new TestData(
                        "test parsing assign number",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "PUSH_N 7 1 5\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getNumber(1, 5, "7")))
                ),
                new TestData(
                        "test parsing assign true",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "PUSH_T 1 5\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getTrue(1, 5)))
                ),
                new TestData(
                        "test parsing assign false",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "PUSH_F 1 5\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getFalse(1, 5)))
                ),
                new TestData(
                        "test parsing mod operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "MOD 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getMod(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing power operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "POWER 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getPower(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing negate operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "NEGATE 1 5\n" +
                                            "PUSH_N 7 1 6\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getNegate(1, 5, getNumber(1, 6, "7"))))
                ),
                new TestData(
                        "test parsing add operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "ADD 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getAdd(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing divide operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "DIVIDE 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getDivide(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing multiply operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "MULTIPLY 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getMultiply(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing subtract operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "SUBTRACT 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getSubtract(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing equals operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "EQUALS 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getEquals(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing greater_than operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "GREATER_THAN 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getGreaterThan(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing less_than operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "LESS_THAN 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getLessThan(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing not_equals operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "NOT_EQUALS 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getNotEquals(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing and operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "AND 1 7\n" +
                                            "PUSH_T 1 5\n" +
                                            "PUSH_T 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getAnd(1, 7, getTrue(1, 5), getTrue(1, 9))))
                ),
                new TestData(
                        "test parsing greater_equal operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "GREATER_EQUAL 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getGreaterEqual(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing less_equal operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "LESS_EQUAL 1 7\n" +
                                            "PUSH_N 1 1 5\n" +
                                            "PUSH_N 2 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getLessEqual(1, 7, getNumber(1, 5, "1"), getNumber(1, 9, "2"))))
                ),
                new TestData(
                        "test parsing not operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "NOT 1 7\n" +
                                            "PUSH_T 1 1 5\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getNot(1, 7, getTrue(1, 5))))
                ),
                new TestData(
                        "test parsing or operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "OR 1 10\n" +
                                            "PUSH_T true 1 5\n" +
                                            "PUSH_T true 1 13\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getOr(1, 10, getTrue(1, 5), getTrue(1, 13))))
                ),
                new TestData(
                        "test parsing concat operator",
                        getBytecode("BLOCK_START 1 1\n" +
                                            "SET 1 3\n" +
                                            "CONCAT 1 7\n" +
                                            "PUSH_S 23 1 5\n" +
                                            "PUSH_S 9 1 9\n" +
                                            "LOAD a 1 1\n" +
                                            "BLOCK_END"),
                        getExpected(getAssign(1, 3,
                                              getVar(1, 1, "a"),
                                              getConcat(1, 7, getString(1, 5, "23"), getString(1, 9, "9"))))
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

    private ModOpExecutor getMod(int l, int c, Executor L, Executor R) {
        return new ModOpExecutor(new SourcePosition(l, c), L, R);
    }

    private PowerOpExecutor getPower(int l, int c, Executor L, Executor R) {
        return new PowerOpExecutor(new SourcePosition(l, c), L, R);
    }

    private NegateOpExecutor getNegate(int l, int c, Executor L) {
        return new NegateOpExecutor(new SourcePosition(l, c), L);
    }

    private FunctionCallExecutor getCall(int l, int c, Executor exe, List<Executor> args) {
        return new FunctionCallExecutor(new SourcePosition(l, c), exe, args);
    }

    private ReturnExecutor getReturn(int l, int c, Executor exp) {
        return new ReturnExecutor(new SourcePosition(l, c), exp);
    }

    private FunctionExecutor getFunction(int l, int c, List<Executor> params, Executor body) {
        return new FunctionExecutor(new SourcePosition(l, c), params, body);
    }

    private AddOpExecutor getAdd(int l, int c, Executor L, Executor R) {
        return new AddOpExecutor(new SourcePosition(l, c), L, R);
    }

    private MultiplyOpExecutor getMultiply(int l, int c, Executor L, Executor R) {
        return new MultiplyOpExecutor(new SourcePosition(l, c), L, R);
    }

    private DivideOpExecutor getDivide(int l, int c, Executor L, Executor R) {
        return new DivideOpExecutor(new SourcePosition(l, c), L, R);
    }

    private SubtractOpExecutor getSubtract(int l, int c, Executor L, Executor R) {
        return new SubtractOpExecutor(new SourcePosition(l, c), L, R);
    }

    private EqualsOpExecutor getEquals(int l, int c, Executor L, Executor R) {
        return new EqualsOpExecutor(new SourcePosition(l, c), L, R);
    }

    private GreaterThanOpExecutor getGreaterThan(int l, int c, Executor L, Executor R) {
        return new GreaterThanOpExecutor(new SourcePosition(l, c), L, R);
    }

    private LessThanOpExecutor getLessThan(int l, int c, Executor L, Executor R) {
        return new LessThanOpExecutor(new SourcePosition(l, c), L, R);
    }

    private NotEqualsOpExecutor getNotEquals(int l, int c, Executor L, Executor R) {
        return new NotEqualsOpExecutor(new SourcePosition(l, c), L, R);
    }

    private AndOpExecutor getAnd(int l, int c, Executor L, Executor R) {
        return new AndOpExecutor(new SourcePosition(l, c), L, R);
    }

    private GreaterEqualOpExecutor getGreaterEqual(int l, int c, Executor L, Executor R) {
        return new GreaterEqualOpExecutor(new SourcePosition(l, c), L, R);
    }

    private LessEqualOpExecutor getLessEqual(int l, int c, Executor L, Executor R) {
        return new LessEqualOpExecutor(new SourcePosition(l, c), L, R);
    }

    private NotOpExecutor getNot(int l, int c, Executor operand) {
        return new NotOpExecutor(new SourcePosition(l, c), operand);
    }

    private OrOpExecutor getOr(int l, int c, Executor L, Executor R) {
        return new OrOpExecutor(new SourcePosition(l, c), L, R);
    }

    private ConcatOpExecutor getConcat(int l, int c, Executor L, Executor R) {
        return new ConcatOpExecutor(new SourcePosition(l, c), L, R);
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

    private TrueExecutor getTrue(int l, int c) {
        return new TrueExecutor(new SourcePosition(l, c));
    }

    private FalseExecutor getFalse(int l, int c) {
        return new FalseExecutor(new SourcePosition(l, c));
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
