package org.bayl.runtime.function;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.Function;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;

public class PrintFunction extends Function {
    @Override
    public BaylObject getDefaultValue(int index) {
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
    public BaylObject eval(Interpreter interpreter, SourcePosition pos) {
        BaylString str = interpreter.getVariable("string", pos).toZString();
        System.out.print(str.toString());
        return str;
    }
}
