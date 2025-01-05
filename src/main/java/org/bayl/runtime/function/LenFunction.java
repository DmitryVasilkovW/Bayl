package org.bayl.runtime.function;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.Function;
import org.bayl.runtime.object.ZemNumber;
import org.bayl.runtime.ZemObject;
import org.bayl.runtime.object.ZemString;

public class LenFunction extends Function {
    @Override
    public ZemObject getDefaultValue(int index) {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 1;
    }

    @Override
    public String getParameterName(int index) {
        return "string";
    }

    @Override
    public ZemObject eval(Interpreter interpreter, SourcePosition pos) {
        ZemString str = interpreter.getVariable("string", pos).toZString();
        return new ZemNumber(str.toString().length());
    }
}
