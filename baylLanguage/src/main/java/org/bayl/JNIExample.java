package org.bayl;

public class JNIExample {

    static {
        System.loadLibrary("jitExecutors");
    }

    private native int generateMultiplicationTemplateInt(int a, int b);
    private native double generateMultiplicationTemplateDouble(double a, double b);

    public static void main(String[] args) {
        JNIExample example = new JNIExample();

        int a = 5;
        int b = 3;

        int result = example.generateMultiplicationTemplateInt(a, b);

        System.out.println("Multiplication result: " + result);

        double aDouble = 1.5;
        double bDouble = 3.0;

        double resultDouble = example.generateMultiplicationTemplateDouble(aDouble, bDouble);

        System.out.println("Multiplication result: " + resultDouble);
    }
}
