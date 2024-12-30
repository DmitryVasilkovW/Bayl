package org.bayl.runtime.function;

import org.bayl.Interpreter;
import org.bayl.SourcePosition;
import org.bayl.runtime.Function;
import org.bayl.runtime.object.ZemArray;
import org.bayl.runtime.ZemObject;

public class ArrayPushFunction extends Function {
    private String[] parameters = {"array", "element"};

    @Override
    public ZemObject eval(Interpreter interpreter, SourcePosition pos) {
        ZemArray array = (ZemArray) interpreter.getVariable("array", pos);
        array.push(interpreter.getVariable("element", pos));
        return array;
    }

    @Override
    public ZemObject getDefaultValue(int index) {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 2;
    }

    @Override
    public String getParameterName(int index) {
        return parameters[index];
    }

}
