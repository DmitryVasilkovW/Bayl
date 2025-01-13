package org.bayl;

public class JNIExample {

    static {
        System.loadLibrary("jitExecutors");
    }

    private native int generateMultiplicationTemplateInt(int a, int b);
    private native double generateMultiplicationTemplateDouble(double a, double b);
    private native int generateAdditionTemplateInt(int a, int b);

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
    }
}
