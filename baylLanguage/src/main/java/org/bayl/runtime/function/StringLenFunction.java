package org.bayl.runtime.function;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.Function;
import org.bayl.runtime.object.BaylNumber;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;
import org.bayl.vm.Environment;

public class StringLenFunction extends Function {
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
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        BaylString str = interpreter.getVariable("string", pos).toBaylString();
        return new BaylNumber(str.toString().length());
    }
}
