package org.bayl.vm;

@FunctionalInterface
public interface TriFunction<P, F, S, R> {
    R apply(P position, F f, S s);
}
