package org.bayl;

import java.io.File;
import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException {
        Interpreter interpreter = new Interpreter();
        interpreter.eval(new File(args[0]));
        //TODO Print nice error messages instead of throwing IOExceptions
    }
}
