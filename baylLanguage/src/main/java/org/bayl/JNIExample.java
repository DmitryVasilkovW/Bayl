package org.bayl;

public class JNIExample {

    static {
        System.loadLibrary("jitExecutors");
    }

    private native int generateMultiplicationTemplateInt(int a, int b);

    public static void main(String[] args) {
        JNIExample example = new JNIExample();

        int a = 5;
        int b = 3;

        int result = example.generateMultiplicationTemplateInt(a, b);

        System.out.println("Multiplication result: " + result);

        int x = 10;
        int y = 3;

        int result2 = example.generateMultiplicationTemplateInt(x, y);

        System.out.println("Multiplication result: " + result2);
    }
}
