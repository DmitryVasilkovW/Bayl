package org.bayl;

public class JNIExample {

    static {
        System.loadLibrary("jitExecutors");
    }

    private native int generateMultiplicationTemplate(int a, int b);

    public static void main(String[] args) {
        JNIExample example = new JNIExample();

        int a = 5;
        int b = 3;

        int result = example.generateMultiplicationTemplate(a, b);

        System.out.println("Multiplication result: " + result);
    }
}
