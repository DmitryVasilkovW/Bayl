package org.bayl.runtime;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;

public class PrintLineFunction extends PrintFunction {
    @Override
    public ZemObject eval(Interpreter interpreter, SourcePosition pos) {
        ZemString str = interpreter.getVariable("string", pos).toZString();
        System.out.println(str.toString());
        return str;
    }
}
