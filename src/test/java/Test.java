import java.io.File;
import java.io.IOException;

import org.bayl.Interpreter;

public class Test {
    public static void main(String[] args) throws IOException {
        Interpreter interpreter = new Interpreter();
        interpreter.eval(new File("sample.zem"));
    }
}
