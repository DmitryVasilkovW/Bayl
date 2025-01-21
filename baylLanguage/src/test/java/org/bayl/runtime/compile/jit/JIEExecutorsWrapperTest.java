package org.bayl.runtime.compile.jit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import util.jit.JITExecutorsWrapperTestDataForMethodsWithOneArg;
import util.jit.JITExecutorsWrapperTestDataForMethodsWithTwoArgs;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JIEExecutorsWrapperTest {

    private final JITExecutorsWrapper jitExecutorsWrapper = new JITExecutorsWrapper();

    @ParameterizedTest(name = "{index} => {arguments}")
    @DisplayName("Test double jit methods: {0}")
    @MethodSource("getAllTestDataForDoubleMethodsWithTwoArgs")
    public void testDoubleJitMethodsWithTwoArgs(JITExecutorsWrapperTestDataForMethodsWithTwoArgs<Double, Double> test) {
        double arg1 = test.arg1();
        double arg2 = test.arg2();

        double result = test.method().apply(arg1, arg2);

        assertEquals(test.expected(), result, test.message());
    }

    @ParameterizedTest(name = "{index} => {arguments}")
    @DisplayName("Test double jit methods: {0}")
    @MethodSource("getAllTestDataForDoubleMethodsWithOneArg")
    public void testDoubleJitMethodsWithOneArg(JITExecutorsWrapperTestDataForMethodsWithOneArg<Double, Double> test) {
        double arg = test.arg();

        double result = test.method().apply(arg);

        assertEquals(test.expected(), result, test.message());
    }

    @ParameterizedTest(name = "{index} => {arguments}")
    @DisplayName("Test boolean jit methods: {0}")
    @MethodSource("getAllTestDataForBooleanMethodsWithTwoArgs")
    public void testBooleanJitMethodsWithTwoArgs(JITExecutorsWrapperTestDataForMethodsWithTwoArgs<Boolean, Boolean> test) {
        boolean arg1 = test.arg1();
        boolean arg2 = test.arg2();

        boolean result = test.method().apply(arg1, arg2);

        assertEquals(test.expected(), result, test.message());
    }

    @ParameterizedTest(name = "{index} => {arguments}")
    @DisplayName("Test boolean jit methods: {0}")
    @MethodSource("getAllTestDataForBooleanMethodsWithOneArg")
    public void testBooleanJitMethodsWithOneArg(JITExecutorsWrapperTestDataForMethodsWithOneArg<Boolean, Boolean> test) {
        boolean arg = test.arg();

        boolean result = test.method().apply(arg);

        assertEquals(test.expected(), result, test.message());
    }

    @ParameterizedTest(name = "{index} => {arguments}")
    @DisplayName("Test double jit methods: {0}")
    @MethodSource("getAllTestRelationDataForDoubleMethodsWithTwoArgs")
    public void testRelationDoubleJitMethodsWithTwoArgs(JITExecutorsWrapperTestDataForMethodsWithTwoArgs<Double, Boolean> test) {
        double arg1 = test.arg1();
        double arg2 = test.arg2();

        boolean result = test.method().apply(arg1, arg2);

        assertEquals(test.expected(), result, test.message());
    }

    private List<JITExecutorsWrapperTestDataForMethodsWithTwoArgs<Double, Double>> getAllTestDataForDoubleMethodsWithTwoArgs() {
        return List.of(
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test multiply",
                        5.0,
                        3.0,
                        15.0,
                        jitExecutorsWrapper::multiply
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test add",
                        5.0,
                        3.0,
                        8.0,
                        jitExecutorsWrapper::add
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test divide with remainder",
                        10.5,
                        3.2,
                        0.899999999999999,
                        jitExecutorsWrapper::divideWithRemainder
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test divide",
                        15.0,
                        3.0,
                        5.0,
                        jitExecutorsWrapper::divide
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test subtract",
                        15.0,
                        3.0,
                        12.0,
                        jitExecutorsWrapper::subtract
                )
        );
    }

    private List<JITExecutorsWrapperTestDataForMethodsWithOneArg<Double, Double>> getAllTestDataForDoubleMethodsWithOneArg() {
        return List.of(
                new JITExecutorsWrapperTestDataForMethodsWithOneArg<>(
                        "test increment",
                        6.0,
                        7.0,
                        jitExecutorsWrapper::increment
                ),
                new JITExecutorsWrapperTestDataForMethodsWithOneArg<>(
                        "test decrement",
                        7.0,
                        6.0,
                        jitExecutorsWrapper::decrement
                )
        );
    }

    private List<JITExecutorsWrapperTestDataForMethodsWithTwoArgs<Boolean, Boolean>> getAllTestDataForBooleanMethodsWithTwoArgs() {
        return List.of(
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test and",
                        true,
                        false,
                        false,
                        jitExecutorsWrapper::and
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test or",
                        true,
                        false,
                        true,
                        jitExecutorsWrapper::or
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test equals for boolean",
                        true,
                        true,
                        true,
                        jitExecutorsWrapper::equalsBoolean
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test not equals for boolean",
                        true,
                        true,
                        false,
                        jitExecutorsWrapper::notEqualsBoolean
                )
        );
    }

    private List<JITExecutorsWrapperTestDataForMethodsWithOneArg<Boolean, Boolean>> getAllTestDataForBooleanMethodsWithOneArg() {
        return List.of(
                new JITExecutorsWrapperTestDataForMethodsWithOneArg<>(
                        "test not",
                        true,
                        false,
                        jitExecutorsWrapper::not
                )
        );
    }

    private List<JITExecutorsWrapperTestDataForMethodsWithTwoArgs<Double, Boolean>> getAllTestRelationDataForDoubleMethodsWithTwoArgs() {
        return List.of(
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test greater or equal",
                        33.0,
                        22.0,
                        true,
                        jitExecutorsWrapper::greaterOrEqual
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test less or equal",
                        1.0,
                        12.0,
                        true,
                        jitExecutorsWrapper::lessOrEqual
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test greater",
                        7.0,
                        3.0,
                        true,
                        jitExecutorsWrapper::greater
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test less",
                        7.0,
                        3.0,
                        false,
                        jitExecutorsWrapper::less
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test equal for double",
                        7.0,
                        7.0,
                        true,
                        jitExecutorsWrapper::equalsDouble
                ),
                new JITExecutorsWrapperTestDataForMethodsWithTwoArgs<>(
                        "test not equal for double",
                        239.0,
                        241.0,
                        true,
                        jitExecutorsWrapper::notEqualsDouble
                )
        );
    }
}
