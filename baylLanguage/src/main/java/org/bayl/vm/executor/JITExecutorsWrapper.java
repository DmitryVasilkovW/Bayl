package org.bayl.vm.executor;

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

    public static void main(String[] args) {
        JITExecutorsWrapper example = new JITExecutorsWrapper();

        double a = 2.0;
        double b = 2.5;

        double resultMultiplication = example.generateMultiplicationTemplate(a, b);
        System.out.println("Multiplication result: " + resultMultiplication);

        double resultAddition = example.generateAdditionTemplate(a, b);
        System.out.println("Addition result: " + resultAddition);

        double resultDivisionRemainder = example.generateDivisionRemainderTemplate(a, b);
        System.out.println("Division remainder result: " + resultDivisionRemainder);

        double resultDivision = example.generateDivisionTemplate(a, b);
        System.out.println("Division: " + resultDivision);

        double resultSubtraction = example.generateSubtractionTemplate(b, a);
        System.out.println("Subtraction: " + resultSubtraction);

        double resultIncrement = example.generateIncrementTemplate(a);
        System.out.println("Increment: " + resultIncrement);

        double resultDecrement = example.generateDecrementTemplate(a);
        System.out.println("Decrement: " + resultDecrement);

        boolean resultAnd = example.generateAndTemplate(false, false);
        System.out.println("AND result: " + resultAnd);

        boolean resultNot = example.generateNotTemplate(false);
        System.out.println("NOT result: " + resultNot);

        boolean resultOr = example.generateOrTemplate(false, false);
        System.out.println("OR result: " + resultOr);

        boolean resultGreaterEquals = example.generateGreaterEqualTemplate(a, b);
        System.out.println("GreaterEquals result: " + resultGreaterEquals);

        boolean resultLessEqual = example.generateLessEqualTemplate(a, b);
        System.out.println("LessEqual result: " + resultLessEqual);
    }
}
