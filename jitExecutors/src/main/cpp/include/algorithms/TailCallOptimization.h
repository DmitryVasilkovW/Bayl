#pragma once

#include <iostream>

class TailCallOptimization {
public:
    TailCallOptimization() = default;

    static double tailRecursiveAddition(double n, double accumulator = 0.0) {
        if (n == 0.0) {
            return accumulator;
        }
        return tailRecursiveAddition(n - 1, n + accumulator);
    }
};
