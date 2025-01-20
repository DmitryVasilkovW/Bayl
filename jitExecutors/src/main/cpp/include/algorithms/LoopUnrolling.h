#pragma once

#include <iostream>

class LoopUnrolling {
public:
    LoopUnrolling() = default;

    static double loopUnrollAddition(char op, double arg1, double arg2, int iterations) {
        double result = 0.0;

        while (iterations >= 5) {
            result += arg1 + arg2;
            result += arg1 + arg2;
            result += arg1 + arg2;
            result += arg1 + arg2;
            result += arg1 + arg2;
            iterations -= 5;
        }

        for (int i = 0; i < iterations; i++) {
            result += arg1 + arg2;
        }

        return result;
    }
};
