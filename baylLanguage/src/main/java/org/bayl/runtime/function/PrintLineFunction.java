package org.bayl.runtime.function;

import org.bayl.model.SourcePosition;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.object.BaylString;
import org.bayl.vm.Environment;

public class PrintLineFunction extends PrintFunction {
    @Override
    public BaylObject eval(Environment interpreter, SourcePosition pos) {
        BaylString str = interpreter.getVariable("string", pos).toBaylString();
        System.out.println(str.toString());
        return str;
    }
}
