package util.jit;

import java.util.function.Function;

public record JITExecutorsWrapperTestDataForMethodsWithOneArg<T, R> (
    String message,
    T arg,
    R expected,
    Function<T, R> method
) {
}
