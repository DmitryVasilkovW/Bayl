package org.bayl.runtime.compile.jit;

public class JITExecutorsWrapper {
    static {
        System.loadLibrary("jitExecutors");
    }

    public native double generateMultiplicationTemplate(double arg1, double arg2);
    public native double generateAdditionTemplate(double arg1, double arg2);
    public native double generateDivisionRemainderTemplate(double arg1, double arg2);
    public native double generateDivisionTemplate(double arg1, double arg2);
    public native double generateSubtractionTemplate(double arg1, double arg2);
    public native double generateIncrementTemplate(double arg1);
    public native double generateDecrementTemplate(double arg1);
    public native boolean generateAndTemplate(boolean arg1, boolean arg2);
    public native boolean generateNotTemplate(boolean arg1);
    public native boolean generateOrTemplate(boolean arg1, boolean arg2);
    public native boolean generateGreaterEqualTemplate(double arg1, double arg2);
    public native boolean generateLessEqualTemplate(double arg1, double arg2);
    public native boolean generateGreaterTemplate(double arg1, double arg2);
    public native boolean generateLessTemplate(double arg1, double arg2);
    public native boolean generateEqualsDoubleTemplate(double arg1, double arg2);
    public native boolean generateNotEqualsDoubleTemplate(double arg1, double arg2);
    public native boolean generateEqualsTemplate(boolean arg1, boolean arg2);
    public native boolean generateNotEqualsTemplate(boolean arg1, boolean arg2);

    public double multiply(double arg1, double arg2) {
        return generateMultiplicationTemplate(arg1, arg2);
    }

    public double add(double arg1, double arg2) {
        return generateAdditionTemplate(arg1, arg2);
    }

    public double divideWithRemainder(double arg1, double arg2) {
        return generateDivisionRemainderTemplate(arg1, arg2);
    }

    public double divide(double arg1, double arg2) {
        return generateDivisionTemplate(arg1, arg2);
    }

    public double subtract(double arg1, double arg2) {
        return generateSubtractionTemplate(arg1, arg2);
    }

    public double increment(double arg1) {
        return generateIncrementTemplate(arg1);
    }

    public double decrement(double arg1) {
        return generateDecrementTemplate(arg1);
    }

    public boolean and(boolean arg1, boolean arg2) {
        return generateAndTemplate(arg1, arg2);
    }

    public boolean not(boolean arg1) {
        return generateNotTemplate(arg1);
    }

    public boolean or(boolean arg1, boolean arg2) {
        return generateOrTemplate(arg1, arg2);
    }

    public boolean greaterOrEqual(double arg1, double arg2) {
        return generateGreaterEqualTemplate(arg1, arg2);
    }

    public boolean lessOrEqual(double arg1, double arg2) {
        return generateLessEqualTemplate(arg1, arg2);
    }

    public boolean greater(double arg1, double arg2) {
        return generateGreaterTemplate(arg1, arg2);
    }

    public boolean less(double arg1, double arg2) {
        return generateLessTemplate(arg1, arg2);
    }

    public boolean equalsDouble(double arg1, double arg2) {
        return generateEqualsDoubleTemplate(arg1, arg2);
    }

    public boolean notEqualsDouble(double arg1, double arg2) {
        return generateNotEqualsDoubleTemplate(arg1, arg2);
    }

    public boolean equalsBoolean(boolean arg1, boolean arg2) {
        return generateEqualsTemplate(arg1, arg2);
    }

    public boolean notEqualsBoolean(boolean arg1, boolean arg2) {
        return generateNotEqualsTemplate(arg1, arg2);
    }
}
