package org.bayl;

public class JNIExample {

    static {
        System.loadLibrary("jitExecutors");
    }

    private native int generateMultiplicationTemplateInt(int arg1, int arg2);
    private native double generateMultiplicationTemplateDouble(double arg1, double arg2);
    private native int generateAdditionTemplateInt(int arg1, int arg2);
    private native double generateAdditionTemplateDouble(double arg1, double arg2);

    public static void main(String[] args) {
        JNIExample example = new JNIExample();

        int a = 5;
        int b = 3;
        double aDouble = 1.5;
        double bDouble = 3.0;

        int resultMultiplication = example.generateMultiplicationTemplateInt(a, b);
        System.out.println("Multiplication result: " + resultMultiplication);

        double resultMultiplicationDouble = example.generateMultiplicationTemplateDouble(aDouble, bDouble);
        System.out.println("Multiplication result: " + resultMultiplicationDouble);

        int resultAddition = example.generateAdditionTemplateInt(a, b);
        System.out.println("Addition result: " + resultAddition);

        double resultAdditionDouble = example.generateAdditionTemplateDouble(aDouble, bDouble);
        System.out.println("Addition result: " + resultAdditionDouble);
    }
}
