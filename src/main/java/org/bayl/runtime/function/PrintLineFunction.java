package org.bayl.runtime.function;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.ZemObject;
import org.bayl.runtime.object.ZemString;

public class PrintLineFunction extends PrintFunction {
    @Override
    public ZemObject eval(Interpreter interpreter, SourcePosition pos) {
        ZemString str = interpreter.getVariable("string", pos).toZString();
        System.out.println(str.toString());
        return str;
    }
}
