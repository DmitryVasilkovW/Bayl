package org.bayl.vm.executor;

public class JITExecutorsWrapper {
    static {
        System.loadLibrary("jitExecutors");
    }

    public native int generateMultiplicationTemplateInt(int arg1, int arg2);
    public native double generateMultiplicationTemplateDouble(double arg1, double arg2);
    public native int generateAdditionTemplateInt(int arg1, int arg2);
    public native double generateAdditionTemplateDouble(double arg1, double arg2);
    public native double generateDivisionRemainderTemplateDouble(double arg1, double arg2);
    public native double generateDivisionTemplateDouble(double arg1, double arg2);

    public static void main(String[] args) {
        JITExecutorsWrapper example = new JITExecutorsWrapper();

        int a = 5;
        int b = 3;
        double aDouble = 3.0;
        double bDouble = 1.4;

        int resultMultiplication = example.generateMultiplicationTemplateInt(a, b);
        System.out.println("Multiplication result: " + resultMultiplication);

        double resultMultiplicationDouble = example.generateMultiplicationTemplateDouble(aDouble, bDouble);
        System.out.println("Multiplication result: " + resultMultiplicationDouble);

        int resultAddition = example.generateAdditionTemplateInt(a, b);
        System.out.println("Addition result: " + resultAddition);

        double resultAdditionDouble = example.generateAdditionTemplateDouble(aDouble, bDouble);
        System.out.println("Addition result: " + resultAdditionDouble);

        double resultDivisionRemainder = example.generateDivisionRemainderTemplateDouble(aDouble, bDouble);
        System.out.println("Division remainder result: " + resultDivisionRemainder);

        double resultDivision = example.generateDivisionTemplateDouble(aDouble, bDouble);
        System.out.println("Division: " + resultDivision);
    }
}
