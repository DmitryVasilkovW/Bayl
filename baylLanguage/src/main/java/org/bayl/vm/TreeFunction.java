package org.bayl.vm;

@FunctionalInterface
public interface TreeFunction<P, F, S, R> {
    R apply(P position, F f, S s);
}
