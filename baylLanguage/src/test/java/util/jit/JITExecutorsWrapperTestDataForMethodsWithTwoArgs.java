package util.jit;

import java.util.function.BiFunction;

public record JITExecutorsWrapperTestDataForMethodsWithTwoArgs<T, R>(
        String message,
        T arg1,
        T arg2,
        R expected,
        BiFunction<T, T, R> method
) {
}
