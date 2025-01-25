package org.bayl.runtime.function.impl.io.output;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylFunction;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.value.BaylString;
import org.bayl.vm.Environment;

public class PrintFunction extends BaylFunction {

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
        return "`string";
    }

    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        BaylString str = interpreter.getVariable("`string", pos).toBaylString();
        System.out.print(str.toString());
        return str;
    }
}
