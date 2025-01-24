package org.bayl.runtime.compile.jit;

public class JITExecutorsWrapper {
    static {
        System.loadLibrary("jitExecutors");
    }

    public static native double generateMultiplicationTemplate(double arg1, double arg2);
    public static native double generateAdditionTemplate(double arg1, double arg2);
    public static native double generateDivisionRemainderTemplate(double arg1, double arg2);
    public static native double generateDivisionTemplate(double arg1, double arg2);
    public static native double generateSubtractionTemplate(double arg1, double arg2);
    public static native double generateIncrementTemplate(double arg1);
    public static native double generateDecrementTemplate(double arg1);
    public static native boolean generateAndTemplate(boolean arg1, boolean arg2);
    public static native boolean generateNotTemplate(boolean arg1);
    public static native boolean generateOrTemplate(boolean arg1, boolean arg2);
    public static native boolean generateGreaterEqualTemplate(double arg1, double arg2);
    public static native boolean generateLessEqualTemplate(double arg1, double arg2);
    public static native boolean generateGreaterTemplate(double arg1, double arg2);
    public static native boolean generateLessTemplate(double arg1, double arg2);
    public static native boolean generateEqualsDoubleTemplate(double arg1, double arg2);
    public static native boolean generateNotEqualsDoubleTemplate(double arg1, double arg2);
    public static native boolean generateEqualsTemplate(boolean arg1, boolean arg2);
    public static native boolean generateNotEqualsTemplate(boolean arg1, boolean arg2);

    public static double multiply(double arg1, double arg2) {
        return generateMultiplicationTemplate(arg1, arg2);
    }

    public static double add(double arg1, double arg2) {
        return generateAdditionTemplate(arg1, arg2);
    }

    public static double divideWithRemainder(double arg1, double arg2) {
        return generateDivisionRemainderTemplate(arg1, arg2);
    }

    public static double divide(double arg1, double arg2) {
        return generateDivisionTemplate(arg1, arg2);
    }

    public static double subtract(double arg1, double arg2) {
        return generateSubtractionTemplate(arg1, arg2);
    }

    public static double increment(double arg1) {
        return generateIncrementTemplate(arg1);
    }

    public static double decrement(double arg1) {
        return generateDecrementTemplate(arg1);
    }

    public static boolean and(boolean arg1, boolean arg2) {
        return generateAndTemplate(arg1, arg2);
    }

    public static boolean not(boolean arg1) {
        return generateNotTemplate(arg1);
    }

    public static boolean or(boolean arg1, boolean arg2) {
        return generateOrTemplate(arg1, arg2);
    }

    public static boolean greaterOrEqual(double arg1, double arg2) {
        return generateGreaterEqualTemplate(arg1, arg2);
    }

    public static boolean lessOrEqual(double arg1, double arg2) {
        return generateLessEqualTemplate(arg1, arg2);
    }

    public static boolean greater(double arg1, double arg2) {
        return generateGreaterTemplate(arg1, arg2);
    }

    public static boolean less(double arg1, double arg2) {
        return generateLessTemplate(arg1, arg2);
    }

    public static boolean equalsDouble(double arg1, double arg2) {
        return generateEqualsDoubleTemplate(arg1, arg2);
    }

    public static boolean notEqualsDouble(double arg1, double arg2) {
        return generateNotEqualsDoubleTemplate(arg1, arg2);
    }

    public static boolean equalsBoolean(boolean arg1, boolean arg2) {
        return generateEqualsTemplate(arg1, arg2);
    }

    public static boolean notEqualsBoolean(boolean arg1, boolean arg2) {
        return generateNotEqualsTemplate(arg1, arg2);
    }
}
